package alexjlockwood.nestedscrolling;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.NestedScrollView.OnScrollChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.support.v7.widget.DividerItemDecoration;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * The sample app's main activity.
 */
public class MainActivity extends AppCompatActivity {

  // True iff the shadow view between the card header and the RecyclerView
  // is currently showing.
  private boolean mIsShowingCardHeaderShadow;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Glide.with(this).asDrawable()
        .load("http://i.imgur.com/zKYUpWa.jpg")
        .apply(RequestOptions.placeholderOf(new ColorDrawable(Color.BLACK)))
        .apply(RequestOptions.centerCropTransform())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into((ImageView) findViewById(R.id.background_image));

    final RecyclerView rv = findViewById(R.id.card_recyclerview);
    final LinearLayoutManager lm = new LinearLayoutManager(this);
    rv.setLayoutManager(lm);
    rv.setAdapter(new LoremIpsumAdapter(this));
    rv.addItemDecoration(new DividerItemDecoration(this, lm.getOrientation()));

    final View cardHeaderShadow = findViewById(R.id.card_header_shadow);
    rv.addOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView rv, int dx, int dy) {
        // Animate the shadow view in/out as the user scrolls so that it
        // looks like the RecyclerView is scrolling beneath the card header.
        final boolean isRecyclerViewScrolledToTop =
            lm.findFirstVisibleItemPosition() == 0
                && lm.findViewByPosition(0).getTop() == 0;
        if (!isRecyclerViewScrolledToTop && !mIsShowingCardHeaderShadow) {
          mIsShowingCardHeaderShadow = true;
          showOrHideView(cardHeaderShadow, true);
        } else if (isRecyclerViewScrolledToTop && mIsShowingCardHeaderShadow) {
          mIsShowingCardHeaderShadow = false;
          showOrHideView(cardHeaderShadow, false);
        }
      }
    });

    final NestedScrollView nsv = findViewById(R.id.nestedscrollview);
    nsv.setOverScrollMode(View.OVER_SCROLL_NEVER);
    nsv.setOnScrollChangeListener(new OnScrollChangeListener() {
      @Override
      public void onScrollChange(
          NestedScrollView nsv, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY == 0 && oldScrollY > 0) {
          // Reset the RecyclerView's scroll position each time the card
          // returns to its starting position.
          rv.scrollToPosition(0);
          cardHeaderShadow.setAlpha(0f);
          mIsShowingCardHeaderShadow = false;
        }
      }
    });
  }

  private static void showOrHideView(View view, boolean shouldShow) {
    view.animate().alpha(shouldShow ? 1f : 0f)
        .setDuration(100)
        .setInterpolator(new DecelerateInterpolator());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
