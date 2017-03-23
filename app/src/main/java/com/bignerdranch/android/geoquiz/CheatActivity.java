package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

    //This version works!

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String INDEX = "com.bignerdranch.android.geoquiz.index";
    private static final String TAG = "CheatActivity";
    private static final String KEY_CHEAT_CHECKER = "cheat_checker";
    private boolean[] mIsCheaterArray= new boolean[5];
    private int mCurrentIndex;
    private static final String KEY_ANSWER = "answer_to_question";
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
//   private TextView mAPILevel;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Log.d("in_newIntent", "answerIsTrue = " + String.valueOf(answerIsTrue));
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    private void setAnswerShownResult(boolean[] isAnswerShown){
        Intent data = new Intent();
        data.putExtra(KEY_CHEAT_CHECKER, isAnswerShown);
        Log.d("in_setAnswerShownResult", "isAnswerShown = " + String.valueOf(isAnswerShown[mCurrentIndex]));
        setResult(RESULT_OK, data);
    }


    public static boolean[] wasAnswerShown(Intent result) {
        Log.d("in_wasAnswerShown", "KEY_CHEAT_CHECKER = " + String.valueOf(result.getBooleanArrayExtra(KEY_CHEAT_CHECKER)));
        return result.getBooleanArrayExtra(KEY_CHEAT_CHECKER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mCurrentIndex = getIntent().getIntExtra(INDEX, -1);
        mIsCheaterArray = getIntent().getBooleanArrayExtra(KEY_CHEAT_CHECKER);
        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = (Button)findViewById(R.id.show_answer_button);
//        mAPILevel = (TextView)findViewById(R.id.api_level);
//        mAPILevel.setText("<API Level " + String.valueOf(Build.VERSION.SDK_INT) + ">");
        if(mCurrentIndex == -1){
            Log.d("in_onCreate", "Something is wrong, no index was passed to CheatActivity via the intent.");
        }
        Log.d("in_onCreate", "mCurrentIndex = " + String.valueOf(mCurrentIndex));
        Log.d("in_onCreate line 63", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAnswer();

                mIsCheaterArray[mCurrentIndex] = true;
                setAnswerShownResult(mIsCheaterArray);
            }
        });

        if (savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(INDEX, -1);
            Log.d("inOnCreate", "mCurrentIndex = " + mCurrentIndex);
            mIsCheaterArray = savedInstanceState.getBooleanArray(KEY_CHEAT_CHECKER);
            if(mIsCheaterArray[mCurrentIndex] == false){
                Log.d("in_onCreate line 78", "mIsCheaterArray[mCurrentIndex] is false");
            }
            Log.d("SIS in_onCreate", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER, false);
            if(mIsCheaterArray[mCurrentIndex] == true){
                setAnswerShownResult(mIsCheaterArray);
                displayAnswer();
            }
        }
        Log.d("Still in_onCreate", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "in onSavedInstanceState");
        Log.d("in_onSaveInstanceState", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));
        savedInstanceState.putInt(INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_CHEAT_CHECKER, mIsCheaterArray);
        savedInstanceState.putBoolean(KEY_ANSWER, mAnswerIsTrue);
    }

    public void displayAnswer(){
        if(mAnswerIsTrue == true){
            mAnswerTextView.setText(R.string.true_button);
        }else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}