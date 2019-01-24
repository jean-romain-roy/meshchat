package prompts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meshchat2.R;


public class OneInput {

    // Attributes
    private AlertDialog myAlertDialog_;
    private Context myContext_;
    private Activity myActivity_;

    private String title_;
    private String subtitle_;

    private RESPONSE responsePayload_;

    // Interface for callbacks
    public interface RESPONSE{
        void text_returned(String text);
    }

    // Constructor
    public OneInput(Context myContext, final String title, final String subtitle, RESPONSE responsePayload){

        this.myContext_ = myContext;
        this.myActivity_ = (Activity) myContext;
        this.responsePayload_ = responsePayload;

        this.title_ = title;
        this.subtitle_ = subtitle;

        buildAlertDialog();
    }

    private void buildAlertDialog(){

        // We build the dialog box
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext_);

        // We fetch our custom layout
        LayoutInflater myInflater = myActivity_.getLayoutInflater();
        final View myDialogView = myInflater.inflate(R.layout.prompt_one_input,null);

        // We set our custom layout in the dialog box
        builder.setView(myDialogView);

        // Fetch UI objects
        Button okButton = myDialogView.findViewById(R.id.ok_button);
        Button cancelButton = myDialogView.findViewById(R.id.cancel_button);

        TextView title_textView = myDialogView.findViewById(R.id.title_textview);
        TextView subtitle_textView = myDialogView.findViewById(R.id.subtitle_textview);
        final EditText password_editText = myDialogView.findViewById(R.id.password_edittext);

        title_textView.setText(title_);
        subtitle_textView.setText(subtitle_);

        // Set the click listeners
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myAlertDialog_.cancel();

                responsePayload_.text_returned(null);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = password_editText.getText().toString().trim().toLowerCase();

                if(text.isEmpty()){
                    Toast.makeText(myContext_,"Empty",Toast.LENGTH_SHORT).show();
                }else{
                    myAlertDialog_.cancel();
                    responsePayload_.text_returned(text);
                }
            }
        });

        // We create the Box
        myAlertDialog_ = builder.create();

        // We show the box
        myAlertDialog_.show();
    }
}
