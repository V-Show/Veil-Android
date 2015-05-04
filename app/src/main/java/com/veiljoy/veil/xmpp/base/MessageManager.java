package com.veiljoy.veil.xmpp.base;

import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.veiljoy.veil.bean.ChartHisBean;
import com.veiljoy.veil.bean.Notice;
import com.veiljoy.veil.database.DBManager;
import com.veiljoy.veil.database.SQLiteTemplate;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.StringUtils;

/**
 * 
 * 消息历史记录，
 *
 */
public class MessageManager {
	private static MessageManager messageManager = null;
	private static DBManager manager = null;

	private MessageManager(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(
				Constants.LOGIN_SET, Context.MODE_PRIVATE);
		String databaseName = sharedPre.getString(Constants.USERNAME, null);
		manager = DBManager.getInstance(context, databaseName);
	}

	public static MessageManager getInstance(Context context) {

		if (messageManager == null) {
			messageManager = new MessageManager(context);
		}

		return messageManager;
	}

	/**
	 * 
	 * 保存消息.
	 *
	 */
	public long saveIMMessage(IMMessage msg) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		if (StringUtils.notEmpty(msg.getmContent())) {
			contentValues.put("content", StringUtils.doEmpty(msg.getmContent()));
		}
		if (StringUtils.notEmpty(msg.getmFrom())) {
			contentValues.put("msg_from",
					StringUtils.doEmpty(msg.getmFrom()));
		}
        //获得消息内容类型
		String scheme=IMMessage.Scheme.ofUri( msg.getmUri()).getScheme();
        contentValues.put("msg_scheme", scheme);
        //获取消息发送类型
        int msgType=msg.getmMessageType();
        if(msgType==IMMessage.SEND){
            msgType=0;
        }
        else if(msgType==IMMessage.RECV){
            msgType=1;
        }

        contentValues.put("msg_type", msgType);
		contentValues.put("msg_time", msg.getmLTime());
		return st.insert("im_msg_history", contentValues);
	}

	/**
	 * 
	 * 更新状态.
	 *
	 */
	public void updateStatus(String id, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		st.updateById("im_msg_his", id, contentValues);
	}

	/**
	 * 
	 * 查找与某人的聊天记录聊天记录
	 * 
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            要查的记录条数
	 */
	public List<IMMessage> getMessageListByFrom(String fromUser, int pageNum,
			int pageSize) {
		if (StringUtils.empty(fromUser)) {
			return null;
		}
		int fromIndex = (pageNum - 1) * pageSize;
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<IMMessage> list = st
				.queryForList(
						new SQLiteTemplate.RowMapper<IMMessage>() {
							@Override
							public IMMessage mapRow(Cursor cursor, int index) {
								IMMessage msg = new IMMessage();
								msg.setmContent(cursor.getString(cursor
                                        .getColumnIndex("content")));
								msg.setmFrom(cursor.getString(cursor
                                        .getColumnIndex("msg_from")));

								int msgType = cursor.getInt(cursor
										.getColumnIndex("msg_type"));

								if (msgType == 0)
									msg.setmMessageType(IMMessage.RECV);
								else
									msg.setmMessageType(IMMessage.SEND);

								// msg.setTime(cursor.getString(cursor
								// .getColumnIndex("msg_time")));

                                String uri=cursor.getString(cursor.getColumnIndex("msg_scheme"));
                                if(uri==null){
                                    uri=IMMessage.Scheme.UNKNOWN.getScheme();
                                }
								msg.setmUri(uri);
								return msg;
							}
						},
						"select content,msg_from,mst_scheme,msg_type,msg_time from im_msg_his where msg_from=? order by msg_time desc limit ? , ? ",
						new String[] { "" + fromUser, "" + fromIndex,
								"" + pageSize });
		return list;

	}

	/**
	 * 
	 * 查找与某人的聊天记录总数
	 * 
	 * @return
	 * @update 2012-7-2 上午9:31:04
	 */
	public int getChatCountWithSb(String fromUser) {
		if (StringUtils.empty(fromUser)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st
				.getCount(
						"select _id,content,msg_from msg_type  from im_msg_his where msg_from=?",
						new String[] { "" + fromUser });
3
	}

	/**
	 * 删除与某人的聊天记录
	 * 
	 * @param fromUser
	 */
	public int delChatHisWithSb(String fromUser) {
		if (StringUtils.empty(fromUser)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_msg_his", "msg_from=?",
				new String[] { "" + fromUser });
	}

	/**
	 * 
	 * 获取最近聊天人聊天最后一条消息和未读消息总数
	 * 
	 * @return
	 */
	public List<ChartHisBean> getRecentContactsWithLastMsg() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<ChartHisBean> list = st
				.queryForList(
						new SQLiteTemplate.RowMapper<ChartHisBean>() {

							@Override
							public ChartHisBean mapRow(Cursor cursor, int index) {
								ChartHisBean notice = new ChartHisBean();
								notice.setId(cursor.getString(cursor
										.getColumnIndex("_id")));
								notice.setContent(cursor.getString(cursor
										.getColumnIndex("content")));
								notice.setFrom(cursor.getString(cursor
										.getColumnIndex("msg_from")));
								notice.setNoticeTime(cursor.getString(cursor
										.getColumnIndex("msg_time")));
								return notice;
							}
						},
						"select m.[_id],m.[content],m.[msg_time],m.msg_from from im_msg_his  m join (select msg_from,max(msg_time) as time from im_msg_his group by msg_from) as tem  on  tem.time=m.msg_time and tem.msg_from=m.msg_from ",
						null);
		for (ChartHisBean b : list) {
			int count = st
					.getCount(
							"select _id from im_notice where status=? and type=? and notice_from=?",
							new String[] { "" + Notice.UNREAD,
									"" + Notice.CHAT_MSG, b.getFrom() });
			b.setNoticeSum(count);
		}
		return list;
	}

}