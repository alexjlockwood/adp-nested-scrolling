package design.shapeshifter.nestedscrolling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A dummy {@link RecyclerView.Adapter} that displays a list of placeholder
 * list items to the user.
 */
class LoremIpsumAdapter extends RecyclerView.Adapter<ViewHolder> {
  private static final int ITEM_COUNT = 5;

  private final LayoutInflater mInflater;

  LoremIpsumAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        mInflater.inflate(R.layout.view_holder_item, parent, false)) {};
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
  }

  @Override
  public int getItemCount() {
    return ITEM_COUNT;
  }
}
