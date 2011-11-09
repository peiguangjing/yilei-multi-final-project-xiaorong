package ctrl;

import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import config.Configure;

import exception.PlayWaveException;

public class SoundPlayer extends Thread{

	private static SoundPlayer sp = null;

	public static SoundPlayer getInstance() {
		if (sp == null) {
			sp = new SoundPlayer();
		}
		return sp;
	}

	private SoundPlayer() {

	}
	
	public void init(){
		
	}
	
	public void play(FileInputStream waveStream) throws PlayWaveException {
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(waveStream);
		} catch (UnsupportedAudioFileException e1) {
			throw new PlayWaveException(e1);
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		// Obtain the information about the AudioInputStream
		AudioFormat audioFormat = audioInputStream.getFormat();
		Info info = new Info(SourceDataLine.class, audioFormat);

		// opens the audio channel
		SourceDataLine dataLine = null;
		try {
			dataLine = (SourceDataLine) AudioSystem.getLine(info);
			dataLine.open(audioFormat, Configure.EXTERNAL_BUFFER_SIZE);
		} catch (LineUnavailableException e1) {
			throw new PlayWaveException(e1);
		}

		// Starts the music :P
		dataLine.start();

		int readBytes = 0;
		byte[] audioBuffer = new byte[Configure.EXTERNAL_BUFFER_SIZE];

		try {
			while (readBytes != -1) {
				readBytes = audioInputStream.read(audioBuffer, 0,
						audioBuffer.length);
				if (readBytes >= 0) {
					dataLine.write(audioBuffer, 0, readBytes);
				}
			}
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		} finally {
			// plays what's left and and closes the audioChannel
			dataLine.drain();
			dataLine.close();
		}
	}
}
