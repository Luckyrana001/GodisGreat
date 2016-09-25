package androidluckyguys.raftingtrackinginhimachal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Rana lucky on 9/23/2016.
 */

public class SecondActivity extends AppCompatActivity {
TextView title ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        title = (TextView)findViewById(R.id.title);

        Intent intent = getIntent();
        String titleValue =
                intent.getStringExtra("titleValue");
        title.setText(titleValue);
    }
    }
