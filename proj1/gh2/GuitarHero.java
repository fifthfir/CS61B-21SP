package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static double frequency(int n) {
        return 440 * Math.pow(2, (n - 24) / 12.0);
    }
    public static void main(String[] args) {
        GuitarString[] guitarStrings = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            GuitarString string = new GuitarString(frequency(i));
            guitarStrings[i] = string;
        }

        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

//        GuitarString[] pluckedGuitarStrings = new GuitarString[37];
        GuitarString string = new GuitarString(200);

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index >= 0) {
                    string = guitarStrings[index];
                    string.pluck();

                }
            }
            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < 37; i++) {
                sample += guitarStrings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++) {
                guitarStrings[i].tic();
            }
        }
    }
}

