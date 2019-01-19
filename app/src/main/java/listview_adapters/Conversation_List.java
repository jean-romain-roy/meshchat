package listview_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.meshchat.R;

import java.util.ArrayList;

import classes.Conversation;

public class Conversation_List extends BaseAdapter {

    // Attributes
    private Context myContext_;
    private ArrayList<Conversation> myConversations_;

    // Constructor
    public Conversation_List(Context myContext, ArrayList<Conversation> myConversations) {

        this.myContext_ = myContext;
        this.myConversations_ = myConversations;
    }

    // Default Methods
    @Override
    public int getCount() {

        if(myConversations_ == null){
            return 0;
        }

        return myConversations_.size();
    }
    @Override
    public Conversation getItem(int position) {

        return myConversations_.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        // When view is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the view supplied
        // by ListView is null.
        if (view == null) { // On create

            // Get the layout
            view = LayoutInflater.from(myContext_).inflate(R.layout.list_conversations, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            viewHolder = new ViewHolder();
            viewHolder.name_ = view.findViewById(R.id.name_items);
            viewHolder.lastMessage_ = view.findViewById(R.id.message_items);

            // Binds the data efficiently with the holder.
            view.setTag(viewHolder);

        }else{    // Populating the already existent

            // Get the ViewHolder back to get fast access to the children's
            viewHolder = (ViewHolder) view.getTag();
        }

        // Set the Name column value
        viewHolder.name_.setText(getItem(position).getCorrespondant());
        viewHolder.lastMessage_.setText(getItem(position).getMessages().get(0).getContent());

        return view;
    }

    // The ViewHolder Class
    static class ViewHolder {
        TextView name_, lastMessage_;
    }
}
