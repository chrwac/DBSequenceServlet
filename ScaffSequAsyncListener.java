import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

public class ScaffSequAsyncListener implements AsyncListener {

	@Override
	public void onComplete(AsyncEvent arg0) throws IOException {
		System.out.println("ScaffSequAsyncListener.onComplete");
		
	}

	@Override
	public void onError(AsyncEvent arg0) throws IOException {
		System.out.println("ScaffSequAsyncListener.onError");
		
	}

	@Override
	public void onStartAsync(AsyncEvent arg0) throws IOException {
		System.out.println("ScaffSequAsyncListener.onStartAsync");
	}

	@Override
	public void onTimeout(AsyncEvent arg0) throws IOException {
		System.out.println("ScaffSequAsyncListener.onTimeout");
	}

}
