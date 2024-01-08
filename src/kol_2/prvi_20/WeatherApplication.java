package src.kol_2.prvi_20;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

interface Updatable {
    void update(float temp, float humidity, float pressure);
}

interface Subject {
    void register(Updatable o);

    void remove(Updatable o);

    void notifyUpdatable();
}

interface Displayable {
    void display();
}

class WeatherDispatcher implements Subject {
    Set<Updatable> updatables;
    float temperature;
    float humidity;
    float pressure;

    public WeatherDispatcher() {
        updatables = new HashSet<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;

        notifyUpdatable();
    }


    @Override
    public void register(Updatable o) {
        updatables.add(o);
    }

    @Override
    public void remove(Updatable o) {
        updatables.remove(o);
    }

    @Override
    public void notifyUpdatable() {
        for (Updatable updatable : updatables) {
            updatable.update(temperature, humidity, pressure);
        }
    }
}

class CurrentConditionsDisplay implements Updatable, Displayable {
    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(Subject weatherStation) {
        weatherStation.register(this);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();
    }

    @Override
    public void display() {
        System.out.println("Temperature: " + temperature + "F");
        System.out.println("Humidity: " + humidity + "%");
    }
}

class ForecastDisplay implements Updatable, Displayable {

    private float currentPressure = 0.0f;
    private float lastPressure;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        lastPressure = currentPressure;
        currentPressure = pressure;
        display();
    }

    @Override
    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure == lastPressure) {
            System.out.println("Same");
        } else if (currentPressure < lastPressure) {
            System.out.println("Cooler");
        } else {
            System.out.println("Improving");
        }
        System.out.println();
    }
}

// 17 TODO: PRERESI
public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}