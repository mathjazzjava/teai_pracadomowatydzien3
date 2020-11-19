package com.matjazz.car;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> carList;

    public CarApi() {
        this.carList = new ArrayList<>();
        carList.add(new Car(1L, "Honda", "Civic", "black"));
        carList.add(new Car(2L, "Mazda", "3", "white"));
        carList.add(new Car(3L, "Hyundai", "IONIQ", "gray"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarsById(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if(first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Zwraca pierwszy znaleziony samochód o podanym kolorze
    /*
    @GetMapping("/color/{color}")
    public ResponseEntity<Car> getCarByColor(@PathVariable String color) {
        Optional<Car> first = carList.stream().filter(car -> car.getColor().equals(color)).findFirst();
        if(first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    */

    @GetMapping("/color/{color}")
    public ResponseEntity<Car> getCarByColor(@PathVariable String color) {
        List<Car> colorCarList = new ArrayList<>();
        for (int i=0; i<carList.size(); i++) {
            if (carList.get(i).getColor().equals(color))
                colorCarList.add(carList.get(i));
        }
        if(colorCarList.size()>0)
            return new ResponseEntity(colorCarList, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        boolean add = carList.add(car);
        if(add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<Car> modCar(@RequestBody Car newCar) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if(first.isPresent()) {
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //Wiem, że dało się to zrobić lepiej, ale chwilowo nie mam pomysłu...
    @PatchMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable long id, @RequestParam(required = false) String mark,
                                         @RequestParam(required = false) String model, @RequestParam(required = false) String color ) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if(first.isPresent()) {
            if(mark == null) mark=first.get().getMark();
            if(model == null) model=first.get().getModel();
            if(color == null) color=first.get().getColor();
            carList.remove(first.get());
            carList.add(new Car(id, mark, model, color));

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if(first.isPresent()) {
            carList.remove(first.get());
            return new ResponseEntity(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
