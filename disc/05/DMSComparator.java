import java.util.Comparator;

class Animal {
    int speak(Dog a) { return 1; }
    int speak(Animal a) { return 2; }
}
class Dog extends Animal {
    int speak(Animal a) { return 3; }
}
class Poodle extends Dog {
    int speak(Dog a) { return 4; }
}
public class DMSComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        int first = o1.speak(new Animal());
        int second = o2.speak(new Animal());
        int third = o1.speak(new Dog());
        int fourth = o2.speak(new Dog());

        if (first == second && third == fourth) {
            return 0;
//      } else if (first > second || third > fourth) {
        } else if (third == 4 || (first == 3 && second == 2)) {
            return 1;
        } else {
            return -1;
        }
    }
}
