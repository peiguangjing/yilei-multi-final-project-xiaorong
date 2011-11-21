package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.AudioBuffer;
import util.VideoBuffer;
import util.VideoReader;

public class Video {

	private File videoFile;
	private File audioFile;

	private Frame[] frames;
	private ArrayList<Shot> shots;
	private LinkedList<Scene> scenes;

	public Video(File video, File audio) throws IOException {
		videoFile = video;
		audioFile = audio;

		int length = VideoReader.getMaxTime(video);
		VideoBuffer.getInstance().init(video, 0);
		AudioBuffer.getInstance().init(audio);

		frames = new Frame[length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = new Frame(i);
		}

		shots = new ArrayList<Shot>();
		scenes = new LinkedList<Scene>();
	}

	public Frame getFrame(int i) {
		if (i < 0 || i >= frames.length) {
			return null;
		}
		return frames[i];
	}

	public Shot addShot(int startTime, int endTime) {
		Shot shot = new Shot(startTime, endTime);
		shots.add(shot);
		return shot;
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
