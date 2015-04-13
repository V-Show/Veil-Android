package com.veiljoy.veil.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhongqihong on 15/4/7.
 */
public class VoiceUtils {

    private String mAudioDir;
    private boolean isRecording = false;
    private String mDefFileName;
    public static VoiceUtils mInstance = null;
    private OnVoiceRecordListener mOnVoiceRecordListener;
    private AudioRecord audioRecord;

    private VoiceUtils() {
        mAudioDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        mDefFileName = "reverseme.pcm";


    }

    public static VoiceUtils getmInstance() {

        if (mInstance == null) {
            synchronized (VoiceUtils.class) {
                if (mInstance == null) {
                    mInstance = new VoiceUtils();
                }
            }
        }

        return mInstance;
    }

    private String generateFileName() {
        mDefFileName = StringUtils.getSequenceId() + ".pcm";
        return mDefFileName;
    }


    public void play(String fileName) {

        Log.v("chatActivity", "recordUtil paly");
        if (fileName == null || fileName.equals(" ")) {
            fileName = mAudioDir + mDefFileName;
        }
        Log.v("chatActivity", "play" + fileName);

        // Get the file we want to playback.
        File file = new File(fileName);
        // Get the length of the audio stored in the file (16 bit so 2 bytes per short)
        // and create a short array to store the recorded audio.
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        Log.v("chatActivity", "recordUtil paly11111");

        try {
            // Create a DataInputStream to read the audio data back from the saved file.
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            // Read the file into the music array.
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }

            Log.v("chatActivity", "recordUtil paly22222");
            // Close the input streams.
            dis.close();


            // Create a new AudioTrack object using the same parameters as the AudioRecord
            // object used to create the file.
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    11025,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength * 2,
                    AudioTrack.MODE_STREAM);
            // Start playback
            audioTrack.play();

            Log.v("chatActivity", "recordUtil paly33333");
            // Write the music buffer to the AudioTrack object
            audioTrack.write(music, 0, musicLength);

            audioTrack.stop();

            Log.v("chatActivity", "recordUtil paly44444");

        } catch (Throwable t) {
            Log.e("AudioTrack", "Playback Failed");
        }
    }

    public String record(String fileName) {


        Log.v("chatActivity", "recordUtil record");
        if (fileName == null || fileName.equals(" ")) {
            fileName = generateFileName();
        }

        String fullName = mAudioDir + fileName;

        int frequency = 11025;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        File file = new File(fullName);


        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            return null;
        }
        // Delete any previous recording.
        if (file.exists())
            file.delete();


        // Create the new file.
        try {
            file.createNewFile();

        } catch (IOException e) {

            return null;
        }

        try {


            // Create a DataOuputStream to write the audio data into the saved file.
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            // Create a new AudioRecord object to record the audio.
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);


            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    frequency, channelConfiguration,
                    audioEncoding, bufferSize);


            short[] buffer = new short[bufferSize];


            audioRecord.startRecording();

            isRecording = true;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++)
                    dos.writeShort(buffer[i]);
            }

            audioRecord.stop();
            dos.close();
            bos.close();
            os.close();


        } catch (Throwable t) {
            return null;
        }

        return fullName;
    }

    public String record() {
        return record(null);
    }

    public void stop() {
        isRecording = false;
    }

    public void startRecord(String fileName) {

        if (isRecording) {
            stop();
        }

        new RecordTask().execute(fileName);
    }


    class RecordTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {

            mOnVoiceRecordListener.onPreRecord();
        }

        @Override
        protected String doInBackground(String... params) {

            mOnVoiceRecordListener.onBackgroundRunning();

            String fileName = params[0];

            return record(fileName);
        }


        @Override
        protected void onPostExecute(String fileName) {

            if (fileName == null) {

            } else {

            }
            mOnVoiceRecordListener.onResult(fileName);
        }

    }

    public void setOnVoiceRecordListener(OnVoiceRecordListener l) {
        this.mOnVoiceRecordListener = l;
    }

    public static interface OnVoiceRecordListener {

        public void onBackgroundRunning();

        // if record successfully ,return a name of the file, otherwise return null.
        public void onResult(String fileName);

        public void onPreRecord();


    }


}
