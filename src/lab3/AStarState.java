package lab3;
import java.util.*;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/

// Класс хранит базовое состояние.
// то есть набор «открытых путевых точек» и другой набор «закрытых путевых точек»

public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    // Ссылка на карту
    private Map2D map;
    private HashMap<Location, Waypoint> openedWaypoints = new HashMap<Location, Waypoint> ();
    private HashMap<Location, Waypoint> closedWaypoints = new HashMap<Location, Waypoint> ();

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    // Инициализация нового объекта для использования пути
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    // Возвращает карту
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    // Метод сканирует все открытые путевые точки и возвращает путевую точку
    // с минимальной общей стоимостью. Если нет открытых путевых точек, этот метод вернет null
    public Waypoint getMinOpenWaypoint()
    {
        // Если нет путевых открытых точек - null
        if (numOpenWaypoints() == 0)
            return null;

        //
        Set<Location> open_waypoint_keys = openedWaypoints.keySet();
        Iterator<Location> i = open_waypoint_keys.iterator();
        Waypoint best = null;
        float best_cost = Float.MAX_VALUE;

        while (i.hasNext())
        {
            Location location = (Location)i.next();
            Waypoint waypoint = openedWaypoints.get(location);
            float waypoint_total_cost = waypoint.getTotalCost();
            if (waypoint_total_cost < best_cost)
            {
                best = openedWaypoints.get(location);
                best_cost = waypoint_total_cost;
            }
        }
        return best;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    // Добавляет путевую точку (или обновляет) в коллекции «открытых путевых точек»
    // Если в местоположении новой путевой точки еще нет открытой путевой точки, новая путевая точка просто добавляется в коллекцию.
    // Если в местоположении новой путевой точки уже есть точка, новая заменяет старую,
    // только если значение «предыдущая стоимость» новой меньше, чем значение «предыдущая стоимость» текущей.
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        Location location = newWP.getLocation();
        if (openedWaypoints!=null && openedWaypoints.containsKey(location))
        {
            Waypoint current_waypoint = openedWaypoints.get(location);
            if (newWP.getPreviousCost() < current_waypoint.getPreviousCost())
            {
                openedWaypoints.put(location, newWP);
                return true;
            }
            return false;
        }
        openedWaypoints.put(location, newWP);
        return true;
    }


    /** Returns the current number of open waypoints. **/
    //Возвращает колличество точек в наборе открытых вершин
    public int numOpenWaypoints()
    {
        return openedWaypoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    // Этот метод перемещает путевую точку в указанное место из открытого списка в закрытый список
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = openedWaypoints.remove(loc);
        closedWaypoints.put(loc, waypoint);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    // Возвращает true, если коллекция закрытых путевых точек содержит путевую точку для указанного места
    public boolean isLocationClosed(Location loc)
    {
        return closedWaypoints.containsKey(loc);
    }
}