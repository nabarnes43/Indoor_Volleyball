package com.example.indoor_volleyball.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.indoor_volleyball.Activities.MainActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;


import com.example.indoor_volleyball.R;
import com.parse.ParseException;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        //Every gym needs an event
        holder.rootView.setTag(event);
//        try {
//            holder.bind(event);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View rootView;
        private TextView tvGymName;
        private ImageView ivGymPhoto;
        private TextView tvEventDateDescription;
        private RatingBar rbGymRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGymName = itemView.findViewById(R.id.tvGymName);
            ivGymPhoto = itemView.findViewById(R.id.ivGymPhoto);
            tvEventDateDescription = itemView.findViewById(R.id.tvEventDateDescription);
            rbGymRating = itemView.findViewById(R.id.rbGymRating);
            rootView = itemView;

            //On click listener for item
            //TODO set up detail view.
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Post post = (Post) v.getTag();
//                    if (post!=null) {
//                        Intent i = new Intent(context, PostDetail.class );
//                        i.putExtra("post", Parcels.wrap(post));
//                        context.startActivity(i);
//                    }
//                }
//            });
        }

        public void bind(Gym gym) throws ParseException {
            Event nextEvent = (Event) gym.getNextEvent();
            tvGymName.setText(gym.getName());
            tvEventDateDescription.setText("Date/Time: " + gym.getNextEvent().getStartTime() + "  " + gym.getNextEvent().getEndTime() + " Details: " + gym.getNextEvent().getDetails());
            rbGymRating.setRating(gym.getRating().floatValue());

            //TODO add functionality to the clicks for gyn.
            tvGymName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).goToGymDetails(gym);
                }
            });

            //TODO Image code
//            ParseFile image = post.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImagePost);
//            }
        }
    }

//    // Clean all elements of the recycler
//    public void clear() {
//        gyms.clear();
//        notifyDataSetChanged();
//    }
//
//    // Add a list of items -- change to type used
//    public void addAll(List<Gym> gymList) {
//        gyms.addAll(gymList);
//        notifyDataSetChanged();
//    }


}
