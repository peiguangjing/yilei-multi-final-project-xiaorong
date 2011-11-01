package ctrl;

import java.io.File;

public class VideoReader {
	private static VideoReader vr = null;

	public static VideoReader getInstance() {
		if (vr == null) {
			vr = new VideoReader();
		}
		return vr;
	}

	File file = null;

	private VideoReader() {
		
	}
}
