package model;

import java.io.File;
import java.io.IOException;

import util.VideoReader;


public class Video {

	private File videoFile;
	private File audioFile;

	private Frame[] frames;

	public Video(File video, File audio) throws IOException {
		videoFile = video;
		audioFile = audio;
		int length = VideoReader.getInstance().init(videoFile).getMaxTime();
		frames = new Frame[length];
	}

	public Frame getFrame(int i) {
		if (i < 0 || i >= frames.length) {
			return null;
		}
		return frames[i];
	}

	public int getLength() {
		return frames.length;
	}

	public File getVideoFile() {
		return videoFile;
	}

	public File getAudioFile() {
		return audioFile;
	}

	public String getVideoPath() {
		return videoFile.getPath();
	}

	public String getAudioPath() {
		return audioFile.getPath();
	}
}
