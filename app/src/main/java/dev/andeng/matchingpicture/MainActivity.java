package dev.andeng.matchingpicture;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView curView = null;
    private int countPair = 0;

    final Integer[] drawable = new Integer[]{
            R.drawable.sample_0,
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4,
            R.drawable.sample_5,
            R.drawable.sample_6,
            R.drawable.sample_7
    };
    Integer[] pos = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7};
    int currentPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetGame(); // Call resetGame() during onCreate to initialize the game

        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentPos < 0) {
                    currentPos = position;
                    curView = (ImageView) view;
                    ((ImageView) view).setImageResource(drawable[pos[position]]);
                } else {
                    if (currentPos == position) {
                        ((ImageView) view).setImageResource(R.drawable.hidden);
                    } else if (pos[currentPos] != pos[position]) {
                        curView.setImageResource(R.drawable.hidden);
                        ((ImageView) view).setImageResource(R.drawable.hidden);
                        Toast.makeText(MainActivity.this, "Not Match!", Toast.LENGTH_LONG).show();
                    } else {
                        ((ImageView) view).setImageResource(drawable[pos[position]]);
                        countPair++; // Increment countPair after every match
                        if (countPair % 2 == 0) {
                            Toast.makeText(MainActivity.this, "Picture Matched!", Toast.LENGTH_LONG).show();
                            // Add any additional logic or UI updates when two cards are matched
                        }
                        if (countPair == 8) {
                            // Add logic for when all pairs are matched
                            Toast.makeText(MainActivity.this, "All Pairs Matched!", Toast.LENGTH_LONG).show();
                        }
                    }
                    currentPos = -1;
                }
            }

        });


        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                Toast.makeText(MainActivity.this, "Puzzle Reset!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Method to initialize the game
    private void initializeGame() {
        resetGame(); // Call resetGame during initialization
        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
    }

    // Method to reset the game
    private void resetGame() {
        countPair = 0;
        currentPos = -1;

        shuffleArrays();

        // Update the GridView with the new shuffled data
        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
    }

    // Method to shuffle the drawable and positions arrays
    private void shuffleArrays() {
        List<Integer> drawableList = Arrays.asList(drawable);
        Collections.shuffle(drawableList);
        drawableList.toArray(drawable);

        List<Integer> posList = Arrays.asList(pos);
        Collections.shuffle(posList);
        posList.toArray(pos);
    }

    public void resetGame(View view) {
    }
}