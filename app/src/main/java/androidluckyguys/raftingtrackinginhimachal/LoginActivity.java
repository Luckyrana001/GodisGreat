package androidluckyguys.raftingtrackinginhimachal;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    View container;
    boolean playAnimations = true;

    private View welcome,profilePic,loginBtn ;EditText emailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, CardViewActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        setAnimationLayout();

    }

    private void setMainLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                //finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
               // finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, CardViewActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }


    /*  All animations starting from here
    alpha change animation on first time window will get focus */
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            showContainer();
            // ShowOtherItemsAnimation();

            playAnimations = false;
        }

    }

    private void showContainer() {
        container.animate().alpha(1f).setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                       /* This listener will start animation after fade in/ alpha animation completes
                        it will increases the clarity of animation.*/
                        super.onAnimationEnd(animation);
                        ShowOtherItemsAnimation();

                    }
                });
    }



    private void ShowOtherItemsAnimation() {
        float startWelcomeX = 0 - welcome.getWidth();
        float endWelcome = welcome.getX();
        ObjectAnimator welcomeAnimator = ObjectAnimator.ofFloat(welcome,View.X , startWelcomeX,endWelcome);
        welcomeAnimator.setDuration(800);

        /*a listener to set welcome text visible only after animation start*/
        welcomeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                welcome.setVisibility(View.VISIBLE);

            }
        });
        // welcomeAnimator.start();


        /*profile pic animation*/
        PropertyValuesHolder scaleXholder = PropertyValuesHolder.ofFloat(View.SCALE_X,1f);
        PropertyValuesHolder scaleYholder = PropertyValuesHolder.ofFloat(View.SCALE_Y,1f);

        ObjectAnimator animateProfilePic = ObjectAnimator.ofPropertyValuesHolder(profilePic, scaleXholder,scaleYholder);
        animateProfilePic.setDuration(500);
        //animateProfilePic.start();

        /*login button animation*/
        ObjectAnimator loginBtnAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this,R.animator.sign_in_anim);
        loginBtnAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setMainLayout();
            }
        });
        loginBtnAnimator.setTarget(loginBtn);
        // loginBtnAnimator.start();


        /*this part will handle the sequencing of animations*/
        AnimatorSet set = new AnimatorSet();
        set.play(welcomeAnimator).after(animateProfilePic);
        set.play(welcomeAnimator).before(loginBtnAnimator);
        set.start();

    }


    private void changeProfilePic() {
        profilePic.animate().rotationY(90).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((ImageView)profilePic).setImageResource(R.drawable.church_logo);
                profilePic.animate().rotationY(0).setDuration(500).setInterpolator(new OvershootInterpolator());
            }
        });
    }
    private void setAnimationLayout() {
        welcome = findViewById(R.id.login_label);
        emailEt = (EditText)findViewById(R.id.email);
        container = findViewById(R.id.container);

        profilePic = findViewById(R.id.logo);

        emailEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !emailEt.getText().toString().equals(""))
                {
                    changeProfilePic();
                }
            }
        });


        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //openSecondAnimation();
            }
        });
    }

}

