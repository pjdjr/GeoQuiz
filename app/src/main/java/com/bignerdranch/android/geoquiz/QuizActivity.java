package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

    //with cheater array

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String DID_THEY_CHEAT = "***cheater?";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String INDEX = "com.bignerdranch.android.geoquiz.index";
    private static final String KEY_CHEAT_CHECKER = "cheat_checker";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mForgiveButton;
    private Button mCheatButton;
    private boolean[] mIsCheaterArray = new boolean[5];

    TextView mQuestionTextView;

    TrueFalse[] mAnswerKey = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;

    private void updateQuestion() {
        int question = mAnswerKey[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();

        int messageResId;

        Log.d("in_checkAnswer", String.valueOf(mIsCheaterArray[mCurrentIndex]));
        if (mIsCheaterArray[mCurrentIndex] && (userPressedTrue == answerIsTrue)) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        //set text to first question in array
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length;
//                mIsCheaterArray[mCurrentIndex] = false;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0)
                    mCurrentIndex = mAnswerKey.length - 1;
                else
                    mCurrentIndex = (mCurrentIndex - 1);
                updateQuestion();
            }
        });

        mForgiveButton = (Button)findViewById(R.id.forgive_button);
        mForgiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheaterArray = new boolean[5];
                Toast.makeText(QuizActivity.this, R.string.forgive_toast, Toast.LENGTH_SHORT).show();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                i.putExtra(INDEX, mCurrentIndex);
                i.putExtra(KEY_CHEAT_CHECKER, mIsCheaterArray);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });
        if (savedInstanceState == null) {
            Log.d("in_QAonCreate", "savedInstanceSt is null");
        }
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheaterArray = savedInstanceState.getBooleanArray(DID_THEY_CHEAT);
            Log.d("SIS in_QAonCreate", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));
            Log.d("in_QAonCreate", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));
        }

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("in_QAonActivityResult", "requestCode = " + String.valueOf(requestCode));
        Log.d("in_QAonActivityResult", "REQUEST_CODE_CHEAT = " + String.valueOf(REQUEST_CODE_CHEAT));
        Log.d("in_QAonActivityResult", "resultCode = " + String.valueOf(resultCode));
        Log.d("in_QAonActivityResult", "RESULT_OK = " + String.valueOf(Activity.RESULT_OK));
        Log.d("in_QAonActivityResult", "RESULT_CANCELED = " + String.valueOf(Activity.RESULT_CANCELED));
        Log.d("in_QAonActivityResult", "data == null? " + String.valueOf(data == null));
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheaterArray = CheatActivity.wasAnswerShown(data);
            Log.d("in_QAonActivityResult", "mIsCheaterArray[mCurrentIndex] = " + String.valueOf(mIsCheaterArray[mCurrentIndex]));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(DID_THEY_CHEAT, mIsCheaterArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_quiz, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}
