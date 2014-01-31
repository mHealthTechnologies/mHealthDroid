package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.util.Log;

public class Utilities {

	/**
	 * Converts an inputStream to a String 
	 * @param is: inputStream to be converted to String
	 * @return the string or a blank string if the is was null
	 */
	public static StringBuilder inputStreamToString(InputStream is) {
		String rLine = "";
		StringBuilder answer = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((rLine = rd.readLine()) != null) {
				answer.append(rLine);
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}
	
	/**
	 * A helper method to convert an InputStream into a String
	 * @param inputStream the inputStream to convert
	 * @return the String or a blank string if the IS was null
	 * @throws IOException
	 */
	public String convertToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * Method to read a file
	 * @param file file to be read
	 * @return a String with the file content
	 */
	public StringBuilder readFile(File file) {

		StringBuilder storedString = new StringBuilder();

		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {

				storedString.append(line);
				storedString.append("\n");
			}
		} catch (Exception e) {

			Log.d("Error", "Couldnt read the file");
		}

		return storedString;
	}
}
