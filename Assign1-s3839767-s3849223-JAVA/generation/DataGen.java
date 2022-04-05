package generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



public class DataGen {

	int largeData = 900; 
	int mediumData = 600;
	int smallData = 150;
	
	String largeDatasetFile = "largeData";
	String mediumDatasetFile = "mediumData";
	String smallDatasetFile = "smallData";
	
	public String id; 
	public String category; 
	public double latitude = 0;
	public double longitude = 0; 
	
	double longitudeMin = 110; 
	double longitudeMax = 180;
	double latitudeMin = 60; 
	double latitudeMax = 100; 
	
	
	
	PrintWriter Formatter = null; 
	public ArrayList<String> dataCollection = new ArrayList<>(); 
	ArrayList<String> addData = new ArrayList<>(); 
	
	public DataGen() {
		
		dataCreation(largeDatasetFile, largeData); 
		dataCreation(mediumDatasetFile, mediumData); 
		dataCreation(smallDatasetFile, smallData); 
	    
		
	}
	
	public void dataCreation(String file, int dataSize) {
		
		dataCollection = create(dataSize);

		  fileGen((file + "smallDatasetAddDelete"), 25, dataCollection);
	      fileGen((file + "mediumDatasetAddDelete"), 50, dataCollection);
	      fileGen((file + "largeDatasetAddDelete"), 90, dataCollection);

        fileSave(file, dataSize, dataCollection);
		
		
	}
	
	
	
	private void fileGen(String file, int dataSize, ArrayList<String> dataCollection) {
		
		 try {
	            Formatter = new PrintWriter(new File(file + ".in"));
	            addData = create(dataSize);
	            int boundCheck = dataCollection.size();
	            for (int i = 0; i < dataSize; i++) {
	                Formatter.printf("D " + "id" + i + " " + dataCollection.get(i));
	                Formatter.printf("A " + "id" + boundCheck + " " + addData.get(i));
	                boundCheck++;
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } finally {
	            Formatter.close();
	        }
		
	}
	
	
	 private ArrayList<String> create(int dataSize) {
	        ArrayList<String> collection = new ArrayList<>();
	        String Point;
	        int random ;
	        int counter = 0;
	        while (counter < dataSize){
	            random = (int) Math.floor(Math.random() * (3));
	            
	            if (random == 0) {
	            	category = "education";
	            }
	            else if (random == 1) {
	            	category = "hospital";
	            }
	            
	            else if (random == 2) {
	            	category = "restaurant";
	            }
	            
	            latitude = randomise(latitudeMin, latitudeMax);
	            longitude = randomise(longitudeMin, longitudeMax);
	            Point = String.format("%s%f %f %n", "-",latitude, longitude);
	            collection.add(category + " " + Point);
	            counter++;
	        }
	        return collection;
	    }
	
	 
	 private double randomise(double minimum, double maximum) {
	        maximum++;
	        double random = Math.random() * (maximum-minimum) + minimum;
	        return random;
	    }
	 
	 public void fileSave(String file, int dataSize, List collection){
	        
	    	try {
	            Formatter = new PrintWriter(new File(file + ".txt"));
	            for (int i = 0; i < dataSize; i++) {
	                Formatter.printf("id" + i + " " + collection.get(i));
	            }
	        }catch (FileNotFoundException e) {
	            System.out.println("Error");
	        }finally {
	            Formatter.close();
	        }
	    }

  	}