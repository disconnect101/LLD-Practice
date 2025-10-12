import java.time.Duration;
import java.time.Instant;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot(SpotType.FOUR_WHEELER_SPOT));
        spots.add(new Spot(SpotType.FOUR_WHEELER_SPOT));
        spots.add(new Spot(SpotType.TWO_WHEELER_SPOT));
        spots.add(new Spot(SpotType.TWO_WHEELER_SPOT));

        SpotManagerFactory spotManagerFactory = new SpotManagerFactory(spots);

        Map<String, EntryGate> entryGateMap = new HashMap<>(Map.of(
                "GATE-1", new EntryGate(spotManagerFactory, new RandomPark()),
                "GATE-2", new EntryGate(spotManagerFactory, new RandomPark()),
                "GATE-3", new EntryGate(spotManagerFactory, new RandomPark())
        ));
        Map<String, ExitGate> exitGateMap = new HashMap<>(Map.of(
                "EX-GATE-1", new ExitGate(spotManagerFactory, new PerMinuteCostCalculator(50)),
                "EX-GATE-2", new ExitGate(spotManagerFactory, new PerMinuteCostCalculator(50)),
                "EX-GATE-3", new ExitGate(spotManagerFactory, new PerMinuteCostCalculator(50))
        ));

        ParkingLot parkingLot = new ParkingLot(entryGateMap, exitGateMap, spots);

        while (true) {
            Ticket ticket = parkingLot.inTakeVehicle(entryGateMap.get("GATE-1"), new Vehicle(VehicleType.FOUR_WHEELER));
            if (ticket == null) {
                System.out.println("Vehicle cant be placed, no spots available");
                return;
            }
            Thread.sleep(1000);
            ticket = parkingLot.inTakeVehicle(entryGateMap.get("GATE-2"), new Vehicle(VehicleType.TWO_WHEELER));
            if (ticket == null) {
                System.out.println("Vehicle cant be placed, no spots available");
                return;
            }

            Thread.sleep(1000);
            int cost = parkingLot.removeVehicle(exitGateMap.get("EX-GATE-3"), ticket);
            System.out.println("Pay Rs." + cost + " for vehicle checkout" );
            Thread.sleep(1000);
        }

    }
}



class ParkingLot {
    private String id;
    private Map<String, EntryGate> entryGates;
    private Map<String, ExitGate> exitGates;
    private List<Spot> spots;

    ParkingLot(Map<String, EntryGate> entryGates, Map<String, ExitGate> exitGates, List<Spot> spots) {
        this.entryGates = entryGates;
        this.exitGates = exitGates;
        this.spots = spots;
    }


    public Ticket inTakeVehicle(EntryGate gate, Vehicle vehicle) {
        return gate.inTakeVehicle(vehicle);
    }

    public int removeVehicle(ExitGate gate, Ticket ticket) {
        return gate.removeVehicle(ticket);
    }
}

class EntryGate {
    private String id;
    private String name;
    private SpotManagerFactory spotManagerFactory;
    private ParkingStrategy parkingStrategy;

    EntryGate(SpotManagerFactory spotManagerFactory, ParkingStrategy parkingStrategy) {
        this.spotManagerFactory = spotManagerFactory;
        this.parkingStrategy = parkingStrategy;
    }

    public Ticket inTakeVehicle(Vehicle vehicle) {
        SpotManager spotManager = this.spotManagerFactory.getSpotManager(this, vehicle.getType(), this.parkingStrategy);
        return spotManager.placeVehicle(vehicle);
    }
}

interface SpotManager {
    public Ticket placeVehicle(Vehicle vehicle);
    public void removeVehicle(Spot spot);
}

class FourWheelerSpotManager implements SpotManager {
    private String id;
    private List<Spot> spots;
    private ParkingStrategy parkingStrategy;
    private EntryGate entryGate;

    FourWheelerSpotManager(EntryGate entryGate, ParkingStrategy parkingStrategy, List<Spot> spots) {
        this.entryGate = entryGate;
        this.parkingStrategy = parkingStrategy;
        this.spots = spots;
    }

    public Ticket placeVehicle(Vehicle vehicle) {
        Spot spot = parkingStrategy.park(this.entryGate, vehicle, this.spots);
        if (spot == null) return null;
        return new Ticket(vehicle, spot);
    }

    public void removeVehicle(Spot spot) {
        spot.freeUpSpot();
    }
}

class TwoWheelerSpotManager implements SpotManager {
    private String id;
    private List<Spot> spots;
    private ParkingStrategy parkingStrategy;
    private EntryGate entryGate;

    TwoWheelerSpotManager(EntryGate entryGate, ParkingStrategy parkingStrategy, List<Spot> spots) {
        this.entryGate = entryGate;
        this.parkingStrategy = parkingStrategy;
        this.spots = spots;
    }

    public Ticket placeVehicle(Vehicle vehicle) {
        Spot spot = parkingStrategy.park(this.entryGate, vehicle, this.spots);
        if (spot == null) return null;
        return new Ticket(vehicle, spot);
    }

    public void removeVehicle(Spot spot) {
        spot.freeUpSpot();
    }
}


class ExitGate {
    private String id;
    private SpotManagerFactory spotManagerFactory;
    private CostCalculator costCalculator;


    ExitGate(SpotManagerFactory spotManagerFactory, CostCalculator costCalculator) {
        this.spotManagerFactory = spotManagerFactory;
        this.costCalculator = costCalculator;
    }

    public int removeVehicle(Ticket ticket) {
        SpotManager spotManager = spotManagerFactory.getSpotManager(null, ticket.getVehicle().getType(), null);
        int cost = this.costCalculator.calculateCost(ticket);
        spotManager.removeVehicle(ticket.getSpot());
        return cost;
    }
}

class Spot {
    private String id;
    private int floor;
    private int number;

    public int getFloor() {
        return floor;
    }

    public int getNumber() {
        return number;
    }

    public SpotType getType() {
        return type;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    private SpotType type;
    private SpotStatus status = SpotStatus.FREE;
    private Vehicle vehicle;

    Spot(SpotType type) {
        this.type = type;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setStatus(SpotStatus status) {
        this.status = status;
    }

    public void freeUpSpot() {
        this.status = SpotStatus.FREE;
    }

    public SpotStatus getStatus() {
        return this.status;
    }
}

enum SpotType {
    TWO_WHEELER_SPOT(0),
    FOUR_WHEELER_SPOT(1);


    private int value;

    SpotType(int value) {

    }
}

enum SpotStatus {
    FREE(0),
    OCCUPIED(1);

    private int value;

    SpotStatus(int value) {
        this.value = value;
    }
}

class Vehicle {
    private int number;
    private VehicleType type;

    Vehicle(VehicleType vehicleType) {
        this.type = vehicleType;
    }

    public VehicleType getType() {
        return this.type;
    }
}

enum VehicleType {
    TWO_WHEELER,
    FOUR_WHEELER;
}



class Ticket {
    private String id;
    private Vehicle vehicle;

    public Spot getSpot() {
        return spot;
    }

    private Spot spot;
    private Instant time;

    Ticket(Vehicle vehicle, Spot spot) {
        this.vehicle = vehicle;
        this.spot = spot;
        this.time = Instant.now();
    }

    public Instant getTime() {
        return time;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }
}

class SpotManagerFactory {
    private List<Spot> twoWheelerSpots;
    private List<Spot> fourWheelerSpots;

    SpotManagerFactory(List<Spot> spots) {
        this.fourWheelerSpots = new ArrayList<>();
        this.twoWheelerSpots = new ArrayList<>();
        for (Spot spot : spots) {
            if (spot.getType() == SpotType.TWO_WHEELER_SPOT) {
                this.twoWheelerSpots.add(spot);
            } else if (spot.getType() == SpotType.FOUR_WHEELER_SPOT) {
                this.fourWheelerSpots.add(spot);
            }
        }
    }

    public SpotManager getSpotManager(EntryGate entryGate, VehicleType vehicleType, ParkingStrategy parkingStrategy) {
        if (vehicleType == VehicleType.FOUR_WHEELER) {
            return new FourWheelerSpotManager(entryGate, parkingStrategy, this.fourWheelerSpots);
        } else if (vehicleType == VehicleType.TWO_WHEELER) {
            return new FourWheelerSpotManager(entryGate, parkingStrategy, this.twoWheelerSpots);
        }
        return null;
    }
}

interface ParkingStrategy {
    public Spot park(EntryGate entryGate, Vehicle vehicle, List<Spot> spots);
}

interface CostCalculator {
    public int calculateCost(Ticket ticket);
}

class RandomPark implements ParkingStrategy {

    @Override
    public Spot park(EntryGate entryGate, Vehicle vehicle, List<Spot> spots) {
        List<Spot> freeSpots = spots.stream().filter((ele) ->
            ele.getStatus() == SpotStatus.FREE
        ).toList();
        if (freeSpots.isEmpty()) {
            return null;
        }
        int randomIdx = new Random().nextInt(freeSpots.size());
        Spot spot = spots.get(randomIdx);
        spot.setVehicle(vehicle);
        spot.setStatus(SpotStatus.OCCUPIED);
        return spot;
    }
}

class PerMinuteCostCalculator implements CostCalculator {

    private int rate;

    PerMinuteCostCalculator(int rate) {
        this.rate = rate;
    }

    @Override
    public int calculateCost(Ticket ticket) {
        Instant instant = ticket.getTime();
        Duration duration = Duration.between(instant, Instant.now());
        double minutes = (double) duration.getSeconds() / 60;
        int roundedMinutes = (int) Math.ceil(minutes);
        return roundedMinutes * this.rate;
    }
}