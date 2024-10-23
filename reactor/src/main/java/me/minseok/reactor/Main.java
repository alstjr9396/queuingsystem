package me.minseok.reactor;

public class Main {

    public static void main(String[] args) {
        Publisher publisher = new Publisher();
        publisher.startFlux()
                .subscribe(System.out::println);

        publisher.startMono()
                .subscribe();

    }

}
