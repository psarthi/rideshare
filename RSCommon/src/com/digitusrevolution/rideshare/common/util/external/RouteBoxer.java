package com.digitusrevolution.rideshare.common.util.external;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Reference - http://google-maps-utility-library-v3.googlecode.com/svn/trunk/routeboxer/docs/examples.html
 * 
 * @name RouteBoxer
 * @version 1.0
 * @copyright (c) 2010 Google Inc.
 * @author Thor Mitchell
 *
 * @fileoverview The RouteBoxer class takes a path, i.e. set of LatLng for a
 * route, and generates a set of LatLngBounds
 * objects that are guaranteed to contain every point within a given distance
 * of that route. These LatLngBounds objects can then be used to generate
 * requests to spatial search services that support bounds filtering (such as
 * the Google Maps Data API) in order to implement search along a route.
 * <br/><br/>
 * RouteBoxer overlays a grid of the specified size on the route, identifies
 * every grid cell that the route passes through, and generates a set of bounds
 * that cover all of these cells, and their nearest neighbours. Consequently
 * the bounds returned will extend up to ~3x the specified distance from the
 * route in places.
 *  
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
public class RouteBoxer {

	// earth's mean radius in km
	private double R; 
	// Two dimensional array representing the cells in the grid overlaid on the path
	private int[][] grid_;
	// Array that holds the latitude coordinate of each vertical grid line
	private List<Double> latGrid_;
	// Array that holds the longitude coordinate of each horizontal grid line
	private List<Double> lngGrid_;
	// Array of bounds that cover the whole route formed by merging cells that
	//  the route intersects first vertically, and then horizontally
	private List<LatLngBounds> boxesX_;
	// Array of bounds that cover the whole route formed by merging cells that
	//  the route intersects first vertically, and then horizontally
	private List<LatLngBounds> boxesY_;
	private static final Logger logger = LogManager.getLogger(RouteBoxer.class.getName());

	/**
	 * Creates a new RouteBoxer
	 *
	 * @constructor
	 */
	public RouteBoxer() {
		this.R = 6371; // earth's mean radius in km
	}

	/**
	 * Generates boxes for a given route and distance
	 *
	 * @param {google.maps.LatLng[] | google.maps.Polyline} path The path along
	 *           which to create boxes. The path object can be either an Array of
	 *           google.maps.LatLng objects or a Maps API v2 or Maps API v3
	 *           google.maps.Polyline object.
	 * @param {Number} range The distance in kms around the route that the generated
	 *           boxes must cover.
	 * @return {google.maps.LatLngBounds[]} An array of boxes that covers the whole
	 *           path.
	 */
	public List<LatLngBounds> box(List<LatLng> path, double range) {
		// Two dimensional array representing the cells in the grid overlaid on the path
		this.grid_ = null;

		// Array that holds the latitude coordinate of each vertical grid line
		this.latGrid_ = new LinkedList<>();

		// Array that holds the longitude coordinate of each horizontal grid line  
		this.lngGrid_ = new LinkedList<>();

		// Array of bounds that cover the whole route formed by merging cells that
		//  the route intersects first horizontally, and then vertically
		this.boxesX_ = new LinkedList<>();

		// Array of bounds that cover the whole route formed by merging cells that
		//  the route intersects first vertically, and then horizontally
		this.boxesY_ = new LinkedList<>();

		// The array of LatLngs representing the vertices of the path
		List<LatLng> vertices = path;

		// Build the grid that is overlaid on the route
		logger.trace("buildGrid_");
		this.buildGrid_(vertices, range);

		// Identify the grid cells that the route intersects
		logger.trace("findIntersectingCells_");
		this.findIntersectingCells_(vertices);

		// Merge adjacent intersected grid cells (and their neighbours) into two sets
		//  of bounds, both of which cover them completely
		logger.trace("mergeIntersectingCells_");
		this.mergeIntersectingCells_();

		// Return the set of merged bounds that has the fewest elements
		return (this.boxesX_.size() <= this.boxesY_.size() ?
				this.boxesX_ :
					this.boxesY_);
	}

	/**
	 * Generates boxes for a given route and distance.
	 *  
	 * IMP**Final grid would cover the whole path and would have one extra cell in all direction
	 * Extra cell is important which will cover all the boundary conditions in rest of the flow
	 *
	 * @param {LatLng[]} vertices The vertices of the path over which to lay the grid
	 * @param {Number} range The spacing of the grid cells.
	 */
	private void buildGrid_(List<LatLng> vertices, double range) {

		// Create a LatLngBounds object that contains the whole path
		LatLngBounds routeBounds = new LatLngBounds();
		for (int i = 0; i < vertices.size(); i++) {
			routeBounds.extend(vertices.get(i));
		}

		// Find the center of the bounding box of the path
		LatLng routeBoundsCenter = routeBounds.getCenter();

		// Starting from the center define grid lines outwards vertically until they
		//  extend beyond the edge of the bounding box by more than one cell
		this.latGrid_.add(routeBoundsCenter.lat());

		// Add lines from the center out to the north (i.e. going upwards)
		this.latGrid_.add(MathUtil.rhumbDestinationPoint(routeBoundsCenter, 0, range).lat());
		for (int i = 2; this.latGrid_.get(i - 2) < routeBounds.getNorthEast().lat(); i++) {
			this.latGrid_.add(MathUtil.rhumbDestinationPoint(routeBoundsCenter, 0, range * i).lat());
		}

		// Add lines from the center out to the south (i.e. going downwards)
		for (int i = 1; this.latGrid_.get(1) > routeBounds.getSouthWest().lat(); i++) {
			this.latGrid_.add(0, MathUtil.rhumbDestinationPoint(routeBoundsCenter, 180, range * i).lat());
		}

		// Starting from the center define grid lines outwards horizontally until they
		//  extend beyond the edge of the bounding box by more than one cell  
		this.lngGrid_.add(routeBoundsCenter.lng());

		// Add lines from the center out to the east (i.e. going right)
		this.lngGrid_.add(MathUtil.rhumbDestinationPoint(routeBoundsCenter, 90, range).lng());
		for (int i = 2; this.lngGrid_.get(i - 2) < routeBounds.getNorthEast().lng(); i++) {
			this.lngGrid_.add(MathUtil.rhumbDestinationPoint(routeBoundsCenter, 90, range * i).lng());
		}

		// Add lines from the center out to the west (i.e. going left)
		for (int i = 1; this.lngGrid_.get(1) > routeBounds.getSouthWest().lng(); i++) {
			this.lngGrid_.add(0,MathUtil.rhumbDestinationPoint(routeBoundsCenter, 270, range * i).lng());
		}

		// Create a two dimensional array representing this grid
		this.grid_ = new int[this.lngGrid_.size()][this.latGrid_.size()];

		logger.trace("lngGrid_.size,latGrid_.size,grid.length[x-LngSize][y-LatSize]:"+lngGrid_.size()+","+latGrid_.size()+"["+grid_.length + ","+grid_[0].length+"]");
	}

	/**
	 * Find all of the cells in the overlaid grid that the path intersects
	 *
	 * @param {LatLng[]} vertices The vertices of the path
	 */
	private void findIntersectingCells_(List<LatLng> vertices) {
		// Find the cell where the path begins
		int[] hintXY = this.getCellCoords_(vertices.get(0));
		logger.trace("Vertex- Cell Cordinate[0]:"+vertices.get(0).toString()+":"+hintXY[0]+","+hintXY[1]);

		// Mark that cell and it's neighbours for inclusion in the boxes
		this.markCell_(hintXY);

		// Work through each vertex on the path identifying which grid cell it is in
		for (int i = 1; i < vertices.size(); i++) {
			// Use the known cell of the previous vertex to help find the cell of this vertex
			int[] gridXY = this.getGridCoordsFromHint_(vertices.get(i), vertices.get(i - 1), hintXY);
			logger.trace("Vertex- Cell Cordinate["+i+"]:"+vertices.get(i).toString()+":"+gridXY[0]+","+gridXY[1]);

			if (gridXY[0] == hintXY[0] && gridXY[1] == hintXY[1]) {
				// This vertex is in the same cell as the previous vertex
				// The cell will already have been marked for inclusion in the boxes
				logger.trace("Same Cell");
				continue;

			} else if ((Math.abs(hintXY[0] - gridXY[0]) == 1 && hintXY[1] == gridXY[1]) ||
					(hintXY[0] == gridXY[0] && Math.abs(hintXY[1] - gridXY[1]) == 1)) {
				// This vertex is in a cell that shares an edge with the previous cell
				// Mark this cell and it's neighbours for inclusion in the boxes
				logger.trace("Neighbour Cell");
				this.markCell_(gridXY);

			} else {
				// This vertex is in a cell that does not share an edge with the previous
				//  cell. This means that the path passes through other cells between
				//  this vertex and the previous vertex, and we must determine which cells
				//  it passes through
				logger.trace("Other Cell");
				this.getGridIntersects_(vertices.get(i - 1), vertices.get(i), hintXY, gridXY);
			}

			// Use this cell to find and compare with the next one
			hintXY = gridXY;
		}
	}

	/**
	 * Find the cell a path vertex is in by brute force iteration over the grid
	 *
	 * @param {LatLng} The latlng of the vertex
	 * @return {Number[]} The cell coordinates of this vertex in the grid containing x and y
	 */ 
	private int[] getCellCoords_ (LatLng latlng) {
		int x,y;
		for (x = 0; this.lngGrid_.get(x) < latlng.lng(); x++) {}
		for (y = 0; this.latGrid_.get(y) < latlng.lat(); y++) {}
		int[] cellCoordinate = {(x - 1), (y - 1)};
		return cellCoordinate;
	}

	/**
	 * Find the cell a path vertex is in based on the known location of a nearby
	 *  vertex. This saves searching the whole grid when working through vertices
	 *  on the polyline that are likely to be in close proximity to each other.
	 *
	 * @param {LatLng} latlng The latlng of the vertex to locate in the grid
	 * @param {LatLng} hintlatlng The latlng of the vertex with a known location
	 * @param {Number[]} hint The cell containing the vertex with a known location
	 * @return {Number[]} The cell coordinates of the vertex to locate in the grid
	 */ 
	private int[] getGridCoordsFromHint_(LatLng latlng, LatLng hintlatlng, int[] hint) {
		logger.trace("latlng,hintlatlng,hint[x][y]:"+latlng.toString()+","+hintlatlng.toString()+":"+hint[0]+","+hint[1]);
		int x = 0;
		int y=0;
		if (latlng.lng() > hintlatlng.lng()) {
			logger.trace("Pass - latlng.lng() > hintlatlng.lng()");
			for (x = hint[0]; this.lngGrid_.get(x + 1) < latlng.lng() ; x++) {
				logger.trace("x:"+x);
			}
		} else {
			logger.trace("Fail - latlng.lng() > hintlatlng.lng()");
			for (x = hint[0]; this.lngGrid_.get(x)  > latlng.lng(); x--) {
				logger.trace("x:"+x);
			}
		}

		if (latlng.lat() > hintlatlng.lat()) {
			logger.trace("Pass - latlng.lat() > hintlatlng.lat()");
			for (y = hint[1]; this.latGrid_.get(y + 1) < latlng.lat() ; y++) {
				logger.trace("y:"+y);
			}
		} else {        
			logger.trace("Fail - latlng.lat() > hintlatlng.lat()");
			for (y = hint[1]; this.latGrid_.get(y) > latlng.lat() ; y--) {
				logger.trace("y:"+y);
			}
		}
		int[] gridCoordinate = {x,y};
		logger.trace("Grid Cordinates [x,y]:"+x+","+y);
		return gridCoordinate;
	}


	/**
	 * Identify the grid squares that a path segment between two vertices
	 * intersects with by:
	 * 1. Finding the bearing between the start and end of the segment
	 * 2. Using the delta between the lat of the start and the lat of each
	 *    latGrid boundary to find the distance to each latGrid boundary
	 * 3. Finding the lng of the intersection of the line with each latGrid
	 *     boundary using the distance to the intersection and bearing of the line
	 * 4. Determining the x-coord on the grid of the point of intersection
	 * 5. Filling in all squares between the x-coord of the previous intersection
	 *     (or start) and the current one (or end) at the current y coordinate,
	 *     which is known for the grid line being intersected
	 *     
	 * @param {LatLng} start The latlng of the vertex at the start of the segment
	 * @param {LatLng} end The latlng of the vertex at the end of the segment
	 * @param {Number[]} startXY The cell containing the start vertex
	 * @param {Number[]} endXY The cell containing the vend vertex
	 */ 
	private void getGridIntersects_(LatLng start,LatLng end, int[] startXY, int[] endXY) {
		LatLng edgePoint;
		int[] edgeXY;
		int i;
		double brng = MathUtil.rhumbBearingTo(start,end);         // Step 1.
		logger.trace("Bearing:"+brng);

		LatLng hint = start;
		int[] hintXY = startXY;

		// Handle a line segment that travels south first
		if (end.lat() > start.lat()) {
			// Iterate over the east to west grid lines between the start and end cells
			for (i = startXY[1] + 1; i <= endXY[1]; i++) {
				// Find the latlng of the point where the path segment intersects with
				//  this grid line (Step 2 & 3)
				edgePoint = this.getGridIntersect_(start, brng, this.latGrid_.get(i));
				logger.trace("EdgePoint:"+edgePoint.toString());

				// Find the cell containing this intersect point (Step 4)
				edgeXY = this.getGridCoordsFromHint_(edgePoint, hint, hintXY);

				// Mark every cell the path has crossed between this grid and the start,
				//   or the previous east to west grid line it crossed (Step 5)
				this.fillInGridSquares_(hintXY[0], edgeXY[0], i - 1);

				// Use the point where it crossed this grid line as the reference for the
				//  next iteration
				hint = edgePoint;
				hintXY = edgeXY;
			}

			// Mark every cell the path has crossed between the last east to west grid
			//  line it crossed and the end (Step 5)
			this.fillInGridSquares_(hintXY[0], endXY[0], i - 1);

		} else {
			// Iterate over the east to west grid lines between the start and end cells
			for (i = startXY[1]; i > endXY[1]; i--) {
				// Find the latlng of the point where the path segment intersects with
				//  this grid line (Step 2 & 3)
				edgePoint = this.getGridIntersect_(start, brng, this.latGrid_.get(i));

				// Find the cell containing this intersect point (Step 4)
				edgeXY = this.getGridCoordsFromHint_(edgePoint, hint, hintXY);

				// Mark every cell the path has crossed between this grid and the start,
				//   or the previous east to west grid line it crossed (Step 5)
				this.fillInGridSquares_(hintXY[0], edgeXY[0], i);

				// Use the point where it crossed this grid line as the reference for the
				//  next iteration
				hint = edgePoint;
				hintXY = edgeXY;
			}

			// Mark every cell the path has crossed between the last east to west grid
			//  line it crossed and the end (Step 5)
			this.fillInGridSquares_(hintXY[0], endXY[0], i);

		}
	}

	/**
	 * Find the latlng at which a path segment intersects with a given
	 *   line of latitude
	 *     
	 * @param {LatLng} start The vertex at the start of the path segment
	 * @param {Number} brng The bearing of the line from start to end
	 * @param {Number} gridLineLat The latitude of the grid line being intersected
	 * @return {LatLng} The latlng of the point where the path segment intersects
	 *                    the grid line
	 */ 
	private LatLng getGridIntersect_(LatLng start, double brng, double gridLineLat) {
		//**Its very important to check the brackets as (a-b)/c is not equal to (a -b/c)
		//Below formula is d = R * (a-b)/c
		double d = this.R * (((MathUtil.toRad(gridLineLat) - MathUtil.toRad(start.lat())) / Math.cos(MathUtil.toRad(brng))));
		logger.trace("MathUtil.toRad(gridLineLat)"+MathUtil.toRad(gridLineLat));
		logger.trace("MathUtil.toRad(start.lat()"+MathUtil.toRad(start.lat()));
		logger.trace("Math.cos(MathUtil.toRad(brng))"+Math.cos(MathUtil.toRad(brng)));
		logger.trace("d:"+d);
		return MathUtil.rhumbDestinationPoint(start,brng, d);
	}

	/**
	 * Mark all cells in a given row of the grid that lie between two columns
	 *   for inclusion in the boxes
	 *     
	 * @param {Number} startx The first column to include
	 * @param {Number} endx The last column to include
	 * @param {Number} y The row of the cells to include
	 */ 
	private void fillInGridSquares_ (int startx, int endx, int y) {
		int x;
		if (startx < endx) {
			for (x = startx; x <= endx; x++) {
				int[] cell = {x,y};
				logger.trace("x,y:"+x+","+y);
				this.markCell_(cell);
			}
		} else {
			for (x = startx; x >= endx; x--) {
				int[] cell = {x,y};
				logger.trace("x,y:"+x+","+y);
				this.markCell_(cell);
			}            
		}      
	}

	/**
	 * Mark a cell and the 8 immediate neighbours for inclusion in the boxes
	 *     
	 * @param {Number[]} square The cell to mark
	 */ 
	private void markCell_(int[] cell) {
		int x = cell[0];
		int y = cell[1];
		this.grid_[x - 1][y - 1] = 1;
		this.grid_[x][y - 1] = 1;
		this.grid_[x + 1][y - 1] = 1;
		this.grid_[x - 1][y] = 1;
		this.grid_[x][y] = 1;
		this.grid_[x + 1][y] = 1;
		this.grid_[x - 1][y + 1] = 1;
		this.grid_[x][y + 1] = 1;
		this.grid_[x + 1][y + 1] = 1;
	}

	/**
	 * Create two sets of bounding boxes, both of which cover all of the cells that
	 *   have been marked for inclusion.
	 *
	 * The first set is created by combining adjacent cells in the same column into
	 *   a set of vertical rectangular boxes, and then combining boxes of the same
	 *   height that are adjacent horizontally.
	 *
	 * The second set is created by combining adjacent cells in the same row into
	 *   a set of horizontal rectangular boxes, and then combining boxes of the same
	 *   width that are adjacent vertically.
	 *     
	 */ 
	private void mergeIntersectingCells_() {
		int x, y;
		LatLngBounds box;

		// The box we are currently expanding with new cells
		LatLngBounds currentBox = null;

		// Traverse the grid a row at a time
		logger.trace("Traverse the grid a row at a time");
		for (y = 0; y < this.grid_[0].length; y++) {
			for (x = 0; x < this.grid_.length; x++) {
				logger.trace("Cell[x][y]:"+x+","+y);
				if (this.grid_[x][y]==1) {
					logger.trace("Cell Marked for Inclusion");
					// This cell is marked for inclusion. If the previous cell in this
					//   row was also marked for inclusion, merge this cell into it's box.
					// Otherwise start a new box.
					int[] cell = {x, y};									
					box = this.getCellBounds_(cell);
					if (currentBox!=null) {
						currentBox.extend(box.getNorthEast());
					} else {
						currentBox = box;
					}

				} else {
					logger.trace("Cell Not marked for Inclusion");
					// This cell is not marked for inclusion. If the previous cell was
					//  marked for inclusion, merge it's box with a box that spans the same
					//  columns from the row below if possible.
					this.mergeBoxesY_(currentBox);
					currentBox = null;
				}
			}
			// If the last cell was marked for inclusion, merge it's box with a matching
			//  box from the row below if possible.
			this.mergeBoxesY_(currentBox);
			currentBox = null;
		}

		logger.trace("Traverse the grid a column at a time");
		// Traverse the grid a column at a time
		for (x = 0; x < this.grid_.length; x++) {
			for (y = 0; y < this.grid_[0].length; y++) {
				logger.trace("Cell[x][y]:"+x+","+y);

				if (this.grid_[x][y]==1) {
					logger.trace("Cell Marked for Inclusion");			
					// This cell is marked for inclusion. If the previous cell in this
					//   column was also marked for inclusion, merge this cell into it's box.
					// Otherwise start a new box.
					if (currentBox!=null) {

						int[] cell = {x, y};
						box = this.getCellBounds_(cell);
						currentBox.extend(box.getNorthEast());
					} else {
						int[] cell = {x, y};
						currentBox = this.getCellBounds_(cell);
					}

				} else {
					logger.trace("Cell Not marked for Inclusion");
					// This cell is not marked for inclusion. If the previous cell was
					//  marked for inclusion, merge it's box with a box that spans the same
					//  rows from the column to the left if possible.
					this.mergeBoxesX_(currentBox);
					currentBox = null;

				}
			}
			// If the last cell was marked for inclusion, merge it's box with a matching
			//  box from the column to the left if possible.
			this.mergeBoxesX_(currentBox);
			currentBox = null;
		}
	}

	/**
	 * Search for an existing box in an adjacent row to the given box that spans the
	 * same set of columns and if one is found merge the given box into it. If one
	 * is not found, append this box to the list of existing boxes.
	 *
	 * @param {LatLngBounds}  The box to merge
	 */ 
	private void mergeBoxesX_(LatLngBounds box) {
		if (box != null) {
			for (int i = 0; i < this.boxesX_.size(); i++) {
				if (this.boxesX_.get(i).getNorthEast().lng() == box.getSouthWest().lng() &&
						this.boxesX_.get(i).getSouthWest().lat() == box.getSouthWest().lat() &&
						this.boxesX_.get(i).getNorthEast().lat() == box.getNorthEast().lat()) {
					this.boxesX_.get(i).extend(box.getNorthEast());
					return;
				}
			}
			this.boxesX_.add(box);
		}
	}

	/**
	 * Search for an existing box in an adjacent column to the given box that spans
	 * the same set of rows and if one is found merge the given box into it. If one
	 * is not found, append this box to the list of existing boxes.
	 *
	 * @param {LatLngBounds}  The box to merge
	 */ 
	private void mergeBoxesY_(LatLngBounds box) {
		if (box != null) {
			for (int i = 0; i < this.boxesY_.size(); i++) {
				if (this.boxesY_.get(i).getNorthEast().lat() == box.getSouthWest().lat() &&
						this.boxesY_.get(i).getSouthWest().lng() == box.getSouthWest().lng() &&
						this.boxesY_.get(i).getNorthEast().lng() == box.getNorthEast().lng()) {
					this.boxesY_.get(i).extend(box.getNorthEast());
					return;
				}
			}
			this.boxesY_.add(box);
		}
	}

	/**
	 * Obtain the LatLngBounds of the a cell on the grid
	 *
	 * @param {Number[]} cell The cell to lookup.
	 * @return {LatLngBounds} The latlngBounds of a cell on the grid
	 */ 
	private LatLngBounds getCellBounds_(int[] cell) {
		logger.trace("cell[x][y]:"+cell[0]+","+cell[1]);
		return new LatLngBounds(
				new LatLng(this.latGrid_.get(cell[1]), this.lngGrid_.get(cell[0])),
				new LatLng(this.latGrid_.get(cell[1] + 1), this.lngGrid_.get(cell[0] + 1)));

	}
}
