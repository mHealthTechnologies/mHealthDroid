package systemManager.guidelines.youtube;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageDownloader {
	
	HashMap<String, Bitmap> imagesHash;
	
	ImageDownloader(){
		imagesHash = new HashMap<String, Bitmap>();
	}
	/**
     * Method to download an image given an url
     * @param url the image url to download
     * @param imageView the view where introduce the selected image
     */
	public void download(String url, ImageView imageView) {
		BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, url, imagesHash);

		if(!imagesHash.containsKey(url)){
			Log.d("Thumbs", "Downloading it");
			task.execute(url);
		}
		else{
			imageView.setImageBitmap(imagesHash.get(url));
		}
	}
}

class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	String url;
	private final WeakReference<ImageView> imageViewReference;
	HashMap<String, Bitmap> images;
	
	/**
     * Constructor. Create a new BitMapDownloader object.
     * @param imageView view where introduce the selected image
     */
	public BitmapDownloaderTask(ImageView imageView, String url, HashMap<String, Bitmap> images) {
		this.url = url;
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.images = images;
	}

	@Override
	/**
     * Download method. Run in the task thread
     * @param params comes from the execute() call. params[0] is the url
     * @return a Bitmap with the image
     */
	protected Bitmap doInBackground(String... params) {
		return downloadBitmap(params[0]);
	}

	@Override
	/**
     * Method to associates the image to the imageView once is downloaded
     */
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null && ((String)imageView.getTag()).equals(url)) {
				imageView.setImageBitmap(bitmap);
				images.put(url, bitmap);
			}
		}
	}

	/**
     * Method to download the bitmap of the desired image
     * @param url the image url
     * @return a Bitmap of the desired image
     */
	static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from "
					+ url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}
}
