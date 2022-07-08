package com.example.indoor_volleyball.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.parse.ParseException;

import java.util.Date;
import java.util.List;

public class UserGymAdapter extends RecyclerView.Adapter<UserGymAdapter.ViewHolder> {
    private Context context;
    private List<Gym> gyms;


    public UserGymAdapter(Context context, List<Gym> gyms) {
        this.context = context;
        this.gyms = gyms;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_gym, parent, false);
        return new ViewHolder(view);    }



    /*Whenever RecyclerView has to show an item to a user it will call onBindViewHolder with this item’s position and ViewHolder.
    Here we should first get the post at this position. Then we can delegate binding to ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gym gym = gyms.get(position);
        //Every gym needs an event
        holder.rootView.setTag(gym);
        try {
            holder.bind(gym);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return gyms.size();
    }

    //TODO SWITCH TO BINDINGS!
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
//
//                }
//            });
        }



        public void bind(Gym gym) throws ParseException {
            // Bind the post data to the view elements
           Event nextEvent = (Event) gym.getNextEvent();
           String eventDetails = "";
           Date startTime = null;
           Date endTime = null;
            try {
                //TODO add this code to events class for cleanliness.
                eventDetails = nextEvent.fetchIfNeeded().getString("details");
                startTime = nextEvent.fetchIfNeeded().getDate("startTime");
                endTime = nextEvent.fetchIfNeeded().getDate("endTime");
            } catch (ParseException e) {
                Toast.makeText(context, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            tvGymName.setText(gym.getName());
            tvEventDateDescription.setText("Date/Time: " + startTime + "  " + endTime+ " Details: " + eventDetails);
            rbGymRating.setRating(gym.getRating().floatValue());


            //TODO add functionality to the clicks for gyn.

//            tvGymName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((MainActivity) context).goToProfileTab(post.getUser());
//                }
//            });

            //TODO Image code

//            ParseFile image = post.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImagePost);
//            }
        }


    }


    // Clean all elements of the recycler
    public void clear() {
        gyms.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Gym> gymList) {
        gyms.addAll(gymList);
        notifyDataSetChanged();
    }







}