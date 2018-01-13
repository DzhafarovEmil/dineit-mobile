package ua.nure.dzhafarov.dineit.screen.restaurant.social

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_social_network.view.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.data.model.SocialNetwork
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick

class SocialNetworkAdapter(private val links: MutableList<SocialNetwork>,
                           private val listener: OnUserClick<SocialNetwork>):
        RecyclerView.Adapter<SocialNetworkHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialNetworkHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_social_network, parent, false)

        return SocialNetworkHolder(view)
    }

    override fun onBindViewHolder(holder: SocialNetworkHolder, position: Int) {
        val link = links[position]
        
        holder.itemView.setOnClickListener { listener.onClick(link) }
        
        holder.itemView.tvSocialNetworkName.text = link.name
        holder.itemView.tvSocialNetworkURL.text = link.url
    }

    override fun getItemCount(): Int {
        return links.size
    }
}