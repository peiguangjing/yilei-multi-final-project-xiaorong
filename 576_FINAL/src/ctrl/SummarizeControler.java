package ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import alg.ColorVectorProcessor;
import alg.Context;
import alg.MotionDetectAlgorithm;
import alg.MotionValuation;
import alg.SoundMergeAlgorithm;
import alg.SoundValuation;

import util.VideoWriter;

import model.Scene;
import model.Shot;
import model.Video;

public class SummarizeControler {
	private static SummarizeControler sc = null;

	public static SummarizeControler getInstance() {
		if (sc == null) {
			sc = new SummarizeControler();
		}
		return sc;
	}

	private Video video = null;

	private int parameter = 0;

	private SummarizeControler() {

	}

	public SummarizeControler init() {
		video = ProjectCenter.getInstance().getVideo();
		return this;
	}

	public void summarize(double percentage) {
		parameter = video.getLength();
		buildShots();
		buildScenes();
		valuation();
		buildNewVideo(percentage, parameter);
		output();
	}

	private void buildShots() {
		System.out.println("Start Cut Shots");
		Context context = new Context(video);
		MotionDetectAlgorithm mda = new MotionDetectAlgorithm(null, context);
		ColorVectorProcessor cvp = new ColorVectorProcessor(mda, context);
		cvp.processAll(parameter);

		video.calMotion(parameter);

		for (int i = 0; i < video.getShots().size(); i++) {
			System.out.println(video.getShots().get(i));
		}
		System.out.println("Finish Cut Shots");
	}

	private void buildScenes() {
		System.out.println("Start Cut Scene: ");
		Context context = new Context(video);
		SoundMergeAlgorithm sma = new SoundMergeAlgorithm(null, context);
		sma.processAll();

		System.out.println("Finish!");
	}

	private void valuation() {
		System.out.println("Start Valuation: ");

		Context context = new Context(video);
		MotionValuation mv = new MotionValuation(null, context);
		SoundValuation sva = new SoundValuation(mv, context);
		sva.processAll();
		for (Scene scene : video.getScenes()) {
			System.out.println(scene);
		}
		System.out.println("Finish!");
	}

	private void buildNewVideo(double percentage, int length) {
		System.out.println("Start Rebuild: ");
		int maxSize = (int) (percentage * 1.05 * length) / 6 * 6;
		// int maxSize = 9;

		int[][] matrix = new int[video.getScenes().size() + 1][maxSize + 1];
		ArrayList<Integer> tArray = new ArrayList<Integer>();

		List<Scene> scenes = video.getScenes();

		for (int i = 0; i <= scenes.size(); i++) {
			matrix[i][0] = 0;
		}

		for (int j = 0; j <= maxSize; j++) {
			matrix[0][j] = 0;
		}

		for (int i = 1; i <= scenes.size(); i++) {
			for (int j = 1; j <= maxSize; j++) {
				matrix[i][j] = matrix[i - 1][j];
				if (scenes.get(i - 1).getLength() <= j) {
					matrix[i][j] = Math.max(matrix[i][j], matrix[i - 1][j
							- scenes.get(i - 1).getLength()]
							+ scenes.get(i - 1).getBalancedValue());
				}
			}
		}

		// for (int i = 0; i <= scenes.size(); i++) {
		// for (int j = 0; j <= maxSize; j++) {
		// System.out.print(matrix[i][j] + " ");
		// }
		// System.out.println();
		// }

		for (int i = scenes.size(); i > 0; i--) {
			if (maxSize >= scenes.get(i - 1).getLength()
					&& matrix[i][maxSize] == matrix[i - 1][maxSize
							- scenes.get(i - 1).getLength()]
							+ scenes.get(i - 1).getBalancedValue()) {
				tArray.add(i - 1);
				// System.out.print((i - 1) + " ");
				maxSize -= scenes.get(i - 1).getLength();
			}
		}

		ArrayList<Integer> results = new ArrayList<Integer>();

		for (int i = tArray.size() - 1; i >= 0; i--) {
			results.add(tArray.get(i));
		}

		// System.out.print(" Length " + result.size());

		video.setList(results);
		System.out.println(" Finish");
	}

	private void output() {
		VideoWriter wr = new VideoWriter(video);
		wr.writeFile();
	}
}
