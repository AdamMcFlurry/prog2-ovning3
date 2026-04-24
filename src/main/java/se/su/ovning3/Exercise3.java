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
			PrintWriter pw = new PrintWriter(fileWriter);
			for (Recording record : recordings) {
				pw.println("<recording>");
				pw.println(getWithAngleBracketLabel("artist", record.getArtist()));
				pw.println(getWithAngleBracketLabel("title", record.getTitle()));
				pw.println(getWithAngleBracketLabel("year", String.valueOf(record.getYear())));
				pw.println("<genres>");
				for (String genre : record.getGenre()) {
					pw.println(getWithAngleBracketLabel("genre", genre));
				}
				pw.println("</genres>");
				pw.println("</recording>");
			}
			pw.close();
 			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.out.printf("%s not found%n", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Questionable use of Object
	private String getWithAngleBracketLabel(String text, String innerText) {
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
				String[] arTiYe = dataRow.split(";");
				
				int amountGenres = Integer.parseInt(reader.readLine());
				Set<String> genres = new HashSet<>();
				for (int y = 0; y < amountGenres; y++) {
					String genre = reader.readLine();
					genres.add(genre);
				}
				Recording newRecording = new Recording(arTiYe[1], arTiYe[0], Integer.parseInt(arTiYe[2]), genres);
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
			FileInputStream fis = new FileInputStream(fileName);
			DataInputStream dis = new DataInputStream(fis);
			int antal = dis.readInt();
			for (int i = 0; i < antal; i++) {
				int year = dis.readInt();
				int month = dis.readInt();
				dis.readInt(); //dis.skipBytes(Integer.BYTES);
				double value = dis.readDouble();
				
				int key = year*100+month;
				if (salesHashMap.containsKey(key)) {
					value+=salesHashMap.get(key);
				// 	double total = salesHashMap.get(year*100+month) + value;
				// 	salesHashMap.put(year*100+month, total);
				// } else {
				// 	salesHashMap.put(year*100+month, value);
				}
				salesHashMap.put(key, value);
			}
			fis.close();
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

