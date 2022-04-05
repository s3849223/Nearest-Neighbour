package nearestNeigh;

import java.util.ArrayList;
import java.util.List;



/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh{

	Node rootNode;
	int depth;
	
	public KDTreeNN() {
		rootNode = null;
		depth = 0;
	}
	
	
    @Override
    public void buildIndex(List<Point> points) {
        
    	rootNode = recBuildIndex(points, rootNode, depth);
    }
    
    public Node recBuildIndex(List<Point> points, Node startNode, int depth) {
    	List<Point> leftPoints = new ArrayList<Point>();
    	List<Point> rightPoints = new ArrayList<Point>();
    	
    	String cord = null;
    	int middleIndex = 0;
    	Point middle;
    	
    	if (depth % 2 != 0) {
    		cord = "lon";
    	}
    	
    	else if (depth % 2 == 0) {
    		cord = "lat";
    	}
    	bubbleSort(points, cord);
    	middleIndex = (int) Math.floor(points.size()/2);
    	middle = points.get(middleIndex);
		Node middleNode = new Node(middle);

		if (middleIndex != 0) {
			for (int i = 0; i < middleIndex; i++) {
				leftPoints.add(points.get(i));

			}
		}

		if (middleIndex != points.size() - 1) {
			for (int i = middleIndex + 1; i < points.size(); i++) {
				rightPoints.add(points.get(i));
			}
		}

		if (startNode == null) {
			startNode = middleNode;
		}


		if (!(leftPoints.isEmpty())) {
			startNode.leftChild = recBuildIndex(leftPoints, startNode.leftChild, depth + 1);
		}

		if (!(rightPoints.isEmpty())) {
			startNode.rightChild = recBuildIndex(rightPoints, startNode.rightChild, depth + 1);
		}


		return startNode;
	}
    
    

    @Override
    public List<Point> search(Point searchTerm, int k) {
    	List<Point> neighbours = new ArrayList<Point>();
    	List<Point> finalKList = new ArrayList<Point>();
    	
	   	Node parentNode = getParentNode(searchTerm, rootNode, depth);
	   	double min = searchTerm.distTo(parentNode.key);
	    neighbours = recSearch(searchTerm, k, rootNode, parentNode, neighbours, finalKList, min, depth);

 
    	
        return neighbours;
    }
    
    private Node getParentNode(Point searchTerm, Node startNode, int depth) {
    	Node parentNode = new Node(null);

    	if (startNode == null && startNode != rootNode){
    		startNode = parentNode;
    	}
    	
    	
    	else if (depth % 2 == 0) {
    	    if (searchTerm.lat < startNode.key.lat) {
    	    	parentNode = startNode;
    	    	getParentNode(searchTerm, startNode.leftChild, depth + 1);
    	    }
    	    else if (searchTerm.lat >= startNode.key.lat) {
    	    	parentNode = startNode;
    	    	getParentNode(searchTerm, startNode.rightChild, depth + 1);
    	    }
        }
        	
    	else if (depth % 2 != 0) {
        	if (searchTerm.lon < startNode.key.lon) {
        		parentNode = startNode;
        		getParentNode(searchTerm, startNode.leftChild, depth + 1);
    	    }
    	    else if (searchTerm.lon >= startNode.key.lon) {
    	    	parentNode = startNode;
    	    	getParentNode(searchTerm, startNode.rightChild, depth + 1);
    	    }
        }
    	
    	return startNode;
    }
    
    public List<Point> recSearch(Point searchTerm, int k, Node startNode, Node closestNode, List<Point> neighbours, List<Point> finalKList,  double min, int depth) {
    	
    	Point dummyPoint = new Point();
    	double dp = 0.0;
    	
    	if ((startNode.key.distTo(searchTerm) < min) && (startNode.key.cat.equals(searchTerm.cat))) {
    		min = startNode.key.distTo(searchTerm);
    		closestNode = startNode;
    		if (!(neighbours.contains(closestNode.key))) {
    			neighbours.add(closestNode.key);
    		}
    	}
    	
    	
    	
    	if (startNode.leftChild != null) {
    		if (depth % 2 == 0) {
    			dummyPoint.lat = startNode.key.lat;
    			dummyPoint.lon = searchTerm.lon;
    			dp = searchTerm.distTo(dummyPoint);
	    		if (dp < min){
	    			recSearch(searchTerm, k, startNode.leftChild, closestNode, neighbours, finalKList, min, depth + 1);
	    		}
	    	}
	    	
	    	
	    	else if (depth % 2 != 0) {
	    		dummyPoint.lat = searchTerm.lat;
    			dummyPoint.lon = startNode.key.lon;
    			dp = searchTerm.distTo(dummyPoint);
	    		if (dp < min){
	    			recSearch(searchTerm, k, startNode.leftChild, closestNode, neighbours, finalKList, min, depth + 1);
	    		}
	    	}
    	}
    		
    	if (startNode.rightChild != null) {
    		if (depth % 2 == 0) {
    			dummyPoint.lat = startNode.key.lat;
    			dummyPoint.lon = searchTerm.lon;
    			dp = searchTerm.distTo(dummyPoint);
    			if (dp < min){
    				recSearch(searchTerm, k, startNode.rightChild, closestNode, neighbours, finalKList, min, depth + 1);
	    		}
	    	}
	    	
	    	
	    	else if (depth % 2 != 0) {
	    		dummyPoint.lat = searchTerm.lat;
    			dummyPoint.lon = startNode.key.lon;
    			dp = searchTerm.distTo(dummyPoint);
    			if (dp < min){
    				recSearch(searchTerm, k, startNode.rightChild, closestNode, neighbours, finalKList, min, depth + 1);
	    		}
	    	}
    	}

    	if (startNode.leftChild == null && startNode.rightChild == null) {
    		if (!(neighbours.isEmpty())) {
				for (int i = 0; i < neighbours.size() - 1; i++) {
					for (int j = 0; j < neighbours.size() - i - 1; j++) {

						if (neighbours.get(j).distTo(searchTerm) > neighbours.get(j + 1).distTo(searchTerm)) {
							Point temp = neighbours.get(j);
							neighbours.set(j, neighbours.get(j + 1));
							neighbours.set(j + 1, temp);

						}
					}
				}
				if (neighbours.size() > k){
					for (int i = 0; i < k; i++) {
						if (!(neighbours.get(i).equals(searchTerm))) {
							finalKList.add(neighbours.get(i));
						}
					}
				}
				else {
					for (int i = 0; i < neighbours.size(); i++){
						if (!(neighbours.get(i).equals(searchTerm))) {
							finalKList.add(neighbours.get(i));
						}
					}
				}

			}
    	}

    	return finalKList;
    }

    @Override
    public boolean addPoint(Point point) {
        
    	if (isPointIn(point) != true) {
    		rootNode = recAddPoint(point, rootNode, depth);
    		return true;
    	}
    	
        return false;
    }
    
    public Node recAddPoint(Point point, Node startNode, int depth) {
    	
    	
    	if (startNode == null) {
    		startNode = new Node(point);
    	}

    	
    	else if (depth % 2 == 0) {
    	    if (point.lat < startNode.key.lat) {
    	    	startNode.leftChild = recAddPoint(point, startNode.leftChild, depth + 1);
    	    }
    	    else if (point.lat >= startNode.key.lat) {
    	    	startNode.rightChild = recAddPoint(point, startNode.rightChild, depth + 1);
    	    }
        }
        	
    	else if (depth % 2 != 0) {
        	if (point.lon < startNode.key.lon) {
        		startNode.leftChild = recAddPoint(point, startNode.leftChild, depth + 1);
    	    }
    	    else if (point.lon >= startNode.key.lon) {
    	    	startNode.rightChild = recAddPoint(point, startNode.rightChild, depth + 1);
    	    }
        }

    	
    	return startNode;

    }

    public boolean deletePoint(Point point) {
        // To be implemented.
    	if (isPointIn(point) != true) {
        	List<Point> xcordPoints = new ArrayList<Point>();
    		rootNode = recdeletePoint(point, rootNode, depth,  xcordPoints);
    		return true;
    	}
    	
        return false;
    }
    
    public Node recdeletePoint(Point point, Node startNode, int depth, List<Point> xcordPoints) {
    	
       Node tempNode = new Node(null); //value of the point being searched for and then searches the right subtree 
    	
	   if((startNode.key.equals(point))) {
		   if(startNode.leftChild == null || startNode.rightChild == null) {
			   startNode = null; 
		   }
		   
		   else if(startNode.rightChild != null) {
			   for(int i = 0; i < 1; i++) {
				   tempNode = startNode; 	
			   } 
			   xcordPoints.add(startNode.key); 
			   startNode.rightChild = recdeletePoint(point, startNode.rightChild, depth, xcordPoints); 
			   
			   bubbleSort(xcordPoints, "lat"); 

		   }
		   
		   else if(startNode.rightChild == null) { 
			   	if( (depth + 1) % 2 == 0) {
			   	
			   		tempNode = startNode.leftChild;
			   		startNode.leftChild = startNode.rightChild; 
			   	}
			   	startNode.rightChild = recdeletePoint(point, startNode.rightChild, depth + 1, xcordPoints); 
  
		   }
	   }
    
	   return startNode; 
    
    }

    @Override
    public boolean isPointIn(Point point) {
        
    	boolean contains = false; 

        contains = recisPointIn(point, rootNode, depth, contains);
         
         return contains; 
    }
    
    public boolean recisPointIn(Point point, Node startNode, int depth, boolean found) {

		if(startNode != null) {

            if((startNode.key.equals(point))){
            	found = true;
            	return found;
            }
            
            if(depth % 2 == 0) {
                    
                if(point.lat < startNode.key.lat) {
                    found = recisPointIn(point, startNode.leftChild, depth + 1, found);
                }
                else if (point.lat >= startNode.key.lat) {  
                	found = recisPointIn(point, startNode.rightChild, depth + 1, found);
                }
            
            }
            else if(depth % 2 != 0) {
                
                if(point.lon < startNode.key.lon) {
                    found = recisPointIn(point, startNode.leftChild, depth + 1, found);
                }
                else if (point.lon >= startNode.key.lon) {                     
                    found = recisPointIn(point, startNode.rightChild, depth + 1, found);
                }
                
            }
        }    
        
        return found;
            
     } 
    
    	
    	
   
    
    private List<Point> bubbleSort(List<Point> points, String cord){
    	
    	if (cord.equals("lat")) {
    		for(int i = 0; i < points.size() - 1; i++) {
				for(int j = 0; j < points.size() - i - 1; j++) {
					
					if (points.get(j).lat > points.get(j + 1).lat) {
						Point temp = points.get(j);
						points.set(j, points.get(j+1));
						points.set(j + 1, temp);
					}
				}
			}
    	}
    	
    	else if (cord.equals("lon")) {
    		for(int i = 0; i < points.size() - 1; i++) {
				for(int j = 0; j < points.size() - i - 1; j++) {
					
					if (points.get(j).lon > points.get(j + 1).lon) {
						Point temp = points.get(j);
						points.set(j, points.get(j+1));
						points.set(j + 1, temp);
					}
				}
			}
    	}
    	
    	
    	
    	return points;
    }

}
