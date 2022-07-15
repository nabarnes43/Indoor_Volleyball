package com.example.indoor_volleyball.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.indoor_volleyball.Activities.MainActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;


import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ItemEventBinding;
import com.example.indoor_volleyball.databinding.ItemGymBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private List<Event> events;
    private int position;


    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new EventAdapter.ViewHolder(ItemEventBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.binding.getRoot().setTag(event);
        try {
            holder.bind(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;


        public ViewHolder(ItemEventBinding b) {
            super(b.getRoot());
            binding = b;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Event event = (Event) v.getTag();
                    if (event != null) {
                        String user = ParseUser.getCurrentUser().getUsername();
                        String creator = event.getCreator().getUsername();
                        Toast.makeText(context, "Creator " + event.getCreator().getUsername(), Toast.LENGTH_SHORT).show();
                        if (user.equals(creator)) {
                            ((MainActivity) context).goToEventDetailsCreating(event);
                        } else {
                            ((MainActivity) context).goToEventDetailsAttending(event);
                        }
                    }
                }
            });
        }

        //TODO string resource and date formatter.
        public void bind(Event event) throws ParseException {
            binding.tvDate.setText("Start time: " + event.getStartTime() + " End Time: " + event.getEndTime());
            binding.tvMinMaxCount.setText(" Min: " + event.getMinCount() + " Max: " + event.getMaxCount());
            binding.tvSkillLevelEvent.setText("Skill Level: " + event.getSkillLevel());

            //TODO Image code
//            ParseFile image = post.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImagePost);
//            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    //TODO ask andrew the difference between this and how I am doing it.

    // Add a list of items -- change to type used
    public void addAll(List<Event> eventList) {
        events.addAll(eventList);
        notifyDataSetChanged();
    }


}