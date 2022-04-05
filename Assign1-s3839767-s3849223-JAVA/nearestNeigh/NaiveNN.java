package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * 
 */
public class NaiveNN implements NearestNeigh{

	List<Point> list;
	
	
	public NaiveNN() {
		list = new ArrayList<Point>();
	}
	
	
	
    @Override
    public void buildIndex(List<Point> points) {
        
    	if (points != null) {
	    	for (int i = 0; i < points.size(); i++) {
	    		if(points.get(i) != null) {
	    			list.add(points.get(i));
	    		}
	    	}
    	}
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        
    	List<Point> neighboursList = new ArrayList<Point>();
    	List<Point> finalKList = new ArrayList<Point>();

    	
    
    	if (searchTerm != null) {
    		// check if needs to be in list
    		//if (list.contains(searchTerm)) {
    			for (int i = 0; i < list.size(); i++) {
    				if (list.get(i).cat.equals(searchTerm.cat)) {
    					if (list.get(i).distTo(searchTerm) != 0) {
	    					neighboursList.add(list.get(i));
	    					

    					}
    				}			
    			}

    			// bubble sort
    			for(int i = 0; i < neighboursList.size() - 1; i++) {
    				for(int j = 0; j < neighboursList.size() - i - 1; j++) {
    					
    					if (neighboursList.get(j).distTo(searchTerm) > neighboursList.get(j + 1).distTo(searchTerm)) {
    						Point temp = neighboursList.get(j);
    						neighboursList.set(j, neighboursList.get(j+1));
    						neighboursList.set(j + 1, temp);
    					}
    				}
    			}
    			
    			for (int i = 0; i < k; i++) {
    				finalKList.add(neighboursList.get(i));
    			}
    			
    		
    	}
    	
    		
        return finalKList;
    }
    
    
    

    @Override
    public boolean addPoint(Point point) {
    	if(point != null) {
	    	if(this.isPointIn(point)) {
	            return false;
	        }
	        list.add(point);
	        return true;
    	}
        return false;
    }

    @Override
    public boolean deletePoint(Point point) {
        
    	if(point != null) {
            if(this.isPointIn(point)) {
                list.remove(point); 
                return true;
            }
        }
        return false; 
    }

    @Override
    public boolean isPointIn(Point point) {
    	if(point != null) {
	    	if(list.contains(point)) { 
	            return true;
	        }
    	}
        return false;	
    }

}
