package agh.ics.oop;

import java.util.ArrayList;
import java.util.Objects;

public class Animal extends AbstractWorldMapElement{
    private MapDirection orientation = MapDirection.NORTH;
    private final IWorldMap map;
    private static final int maxi=4;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();


    public Animal(IWorldMap map){
        this.map = map;
        this.position = new Vector2d(2,2);
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
    }

    public Animal(){
        this.position = new Vector2d(2,2);
        this.map = new RectangularMap(maxi, maxi);
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer:observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
    @Override
    public String toString() {
        String temp = "";
        switch (orientation){
            case NORTH -> temp="N";
            case EAST -> temp="E";
            case SOUTH -> temp="S";
            case WEST -> temp="W";
        }
        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return orientation == animal.orientation && Objects.equals(position, animal.position) && Objects.equals(map, animal.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientation, position, map);
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public IWorldMap getMap(){return map;}


    public void move(MoveDirection[] directions){
        for (MoveDirection direction:directions) {
            switch (direction) {
                case RIGHT:
                    orientation = orientation.next();
                    break;
                case LEFT:
                    orientation = orientation.previous();
                    break;
                case FORWARD:
                    if (map.canMoveTo(orientation.toUnitVector().add(position))) {
                        position = position.add(orientation.toUnitVector());
                        positionChanged(position.add(orientation.toUnitVector().opposite()),position);
                    }
                    break;
                case BACKWARD:
                    if (map.canMoveTo(orientation.toUnitVector().opposite().add(position))) {
                        position = position.add(orientation.toUnitVector().opposite());
                        positionChanged(position.add(orientation.toUnitVector()),position);
                    }
                    break;
            }
        }
    }
    public void move(MoveDirection direction) {
        MoveDirection[] directions = {direction};
        move(directions);
    }
    @Override
    public String getImageUrl(){
        switch (orientation){
            case NORTH -> {
                return "up.png";
            }
            case EAST -> {
                return "right.png";
            }
            case SOUTH -> {
                return "down.png";
            }
            case WEST -> {
                return "left.png";
            }
            default -> throw new IllegalArgumentException("Direction not found");
        }
    }
}
