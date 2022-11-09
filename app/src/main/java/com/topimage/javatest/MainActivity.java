package com.topimage.javatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.topimage.javatest.formstep.OrderComplete;
import com.topimage.javatest.formstep.OrderDeliverd;
import com.topimage.javatest.formstep.OrderProcessed;
import com.topimage.javatest.formstep.PaySucess;

import ernestoyaquello.com.verticalstepperform.Step;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class MainActivity extends AppCompatActivity implements StepperFormListener {

    private PaySucess paySucess;
    private OrderProcessed orderProcessed;
    private OrderDeliverd orderDeliverd;
    private OrderComplete orderComplete;

    private VerticalStepperFormView verticalStepperForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Random test button it can be pass all step or fail in between
        findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnRandomResponce();
            }
        });

        //always pass test button it will always pass all the steps
        findViewById(R.id.testButtonpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allwaysPassResponce();
            }
        });


        // Create the steps.
        paySucess = new PaySucess("Payment Successful");
        orderProcessed = new OrderProcessed("Order Processed");
        orderDeliverd = new OrderDeliverd(("Order Delivered"));

        // Find the form view, set it up and initialize it.
        verticalStepperForm = findViewById(R.id.stepper_form);
        initStepper();
    }

    @Override
    public void onCompletedForm() {
        Toast.makeText(this, "Process Complete", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelledForm() {
        Toast.makeText(this, "something went wrong ! Process Cancel", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStepAdded(int index, Step<?> addedStep) {

    }

    @Override
    public void onStepRemoved(int index) {

    }


    public void returnRandomResponce(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int random = (int)(Math.random() * 50 + 1);
                if(random % 2 == 0){
                    if(verticalStepperForm.getTotalNumberOfSteps() > verticalStepperForm.getOpenStepPosition()){
                        verticalStepperForm.goToNextStep(true);
                        returnRandomResponce();
                    }else{
                        verticalStepperForm.cancelFormCompletionOrCancellationAttempt();
                    }
                }else{
                    verticalStepperForm.cancelForm();
                }
            }
        }, 1000);
    }

    public void allwaysPassResponce(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(verticalStepperForm.getTotalNumberOfSteps() > verticalStepperForm.getOpenStepPosition()){
                    verticalStepperForm.goToNextStep(true);
                    allwaysPassResponce();
                }else{
                    verticalStepperForm.cancelFormCompletionOrCancellationAttempt();
                }
            }
        }, 1000);
    }

    public void initStepper(){
        verticalStepperForm
                .setup(this, paySucess, orderProcessed, orderDeliverd)
                .displayStepButtons(false)
                .displayBottomNavigation(false)
                .displayDifferentBackgroundColorOnDisabledElements(true)
                .displayNextButtonInLastStep(false)
                .confirmationStepTitle("Order Complete")
                .confirmationStepSubtitle("Your order has been received")
                .init();
    }
}