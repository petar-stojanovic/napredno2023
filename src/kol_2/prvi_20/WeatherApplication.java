package src.kol_2.prvi_20;

import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

interface Display {
    void display();

    void add(Measurement measurement);
}

class ForecastDisplay implements Display {

    float pressure;
    String forecast;
    WeatherDispatcher weatherDispatcher;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
        pressure = 0;
    }

    @Override
    public void display() {
        System.out.printf("Forecast: %s\n", forecast);
    }

    @Override
    public void add(Measurement measurement) {
        if (pressure < measurement.pressure) {
            forecast = "Improving";
        } else if (pressure > measurement.pressure) {
            forecast = "Cooler";
        } else {
            forecast = "Same";
        }
        this.pressure = measurement.pressure;
        display();
    }
}

class CurrentConditionsDisplay implements Display {
    float temperature;
    float humidity;

    WeatherDispatcher weatherDispatcher;

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
    }

    @Override
    public void display() {
        System.out.printf("Temperature: %.1fF\n", temperature);
        System.out.printf("Humidity: %.1f%%\n", humidity);
    }

    @Override
    public void add(Measurement measurement) {
        temperature = measurement.temperature;
        humidity = measurement.humidity;
        display();
    }
}

class Measurement {
    float temperature;
    float humidity;
    float pressure;

    public Measurement(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}

class WeatherDispatcher {

    Set<Display> displays;

    public WeatherDispatcher() {
        this.displays = new HashSet<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        Measurement measurement = new Measurement(temperature, humidity, pressure);
        updateDisplays(measurement);
    }

    private void updateDisplays(Measurement measurement) {
        displays.forEach(it -> it.add(measurement));
    }

    public void remove(Display display) {
        displays.remove(display);
    }

    public void register(Display display) {
        displays.add(display);
    }
}

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
                System.out.println();
            }
        }
    }
}