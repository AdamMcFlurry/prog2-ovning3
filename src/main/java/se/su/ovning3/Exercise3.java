package se.su.ovning3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Exercise3 {

	private final List<Recording> recordings = new ArrayList<>();

	public void exportRecordings(String fileName) {
		try{
			FileWriter fileWriter = new FileWriter(fileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("<recording>");
			for (Recording record : recordings) {
				printWriter.println(getWithAngleBracketLabel("artist", record.getArtist()));
				printWriter.println(getWithAngleBracketLabel("title", record.getTitle()));
				printWriter.println(getWithAngleBracketLabel("year", record.getYear()));
				printWriter.println("<genres>");
				for (String genre : record.getGenre()) {
					printWriter.println(getWithAngleBracketLabel("genre", genre));
				}
				printWriter.println("</genres>");
			}
			printWriter.println("</recording>");
			printWriter.close();
 			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.out.printf("%s not found%n", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Questionable...
	private String getWithAngleBracketLabel(String text, Object innerText) {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(text).append(">");
		sb.append(innerText);
		sb.append("</").append(text).append(">");
		return sb.toString();
	}

	public void importRecordings(String fileName) {
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(fileReader);
			int amount = Integer.parseInt(reader.readLine());
			for (int i = 0; i < amount; i++) {
				String dataRow = reader.readLine();
				String[] artistTitleYear = dataRow.split(";");

				Set<String> genres = new HashSet<>();
				int amountGenres = Integer.parseInt(reader.readLine());
				for (int y = 0; y < amountGenres; y++) {
					String genre = reader.readLine();
					genres.add(genre);
				}
				Recording newRecording = new Recording(artistTitleYear[1], artistTitleYear[0], Integer.parseInt(artistTitleYear[2]), genres);
				recordings.add(newRecording);
			}
			fileReader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.printf("%s not found%n", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, Double> importSales(String fileName) {
		Map<Integer, Double> salesHashMap = new HashMap<>();
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
			int antal = dis.readInt();
			for (int i = 0; i < antal; i++) {
				int year = dis.readInt();
				int month = dis.readInt();
				dis.readInt();
				double value = dis.readDouble();
				boolean valueExistsInMap = salesHashMap.get(year*100+month) != null;
				if (valueExistsInMap) {
					double total = salesHashMap.get(year*100+month) + value;
					salesHashMap.put(year*100+month, total);
				} else {
					salesHashMap.put(year*100+month, value);
				}
			}
			dis.close();
		} catch (FileNotFoundException e) {
			System.out.printf("%s not found%n", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return salesHashMap;
	}
	

	public List<Recording> getRecordings() {
		return Collections.unmodifiableList(recordings);
	}

	public void setRecordings(List<Recording> recordings) {
		this.recordings.clear();
		this.recordings.addAll(recordings);
	}

}

