package com.bateman.richard.reminderapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

// Article about implementing swiping and buttons.
// https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28


public class SwipeController extends ItemTouchHelper.Callback {
//
//    enum ButtonsState {
//        GONE,
//        LEFT_VISIBLE,
//        RIGHT_VISIBLE,
//    }
//
//    private abstract class SwipeControllerActions {
//        public void onLeftClicked(int position) {}
//
//        public void onRightClicked(int position) {}
//    }

    private static final int BUTTON_WIDTH=160;
    private boolean swipeBack = false;
    //private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    //private SwipeControllerActions buttonsActions = null;

    private ReminderEntryRecyclerViewAdapter m_recycleViewAdapter;
    private ReminderCollection m_reminderCollection;

    public SwipeController(ReminderCollection collection, ReminderEntryRecyclerViewAdapter adapter) {
        m_reminderCollection = collection;
        m_recycleViewAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        return makeMovementFlags(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        ReminderEntryRecyclerViewAdapter.ReminderEntryViewHolder reminderViewHolder = (ReminderEntryRecyclerViewAdapter.ReminderEntryViewHolder) viewHolder;
        ReminderEntry entry = reminderViewHolder.getReminderEntry();
        int position = reminderViewHolder.getAdapterPosition();

        m_reminderCollection.removeReminderAt(position);

        if(direction == ItemTouchHelper.LEFT) {
            entry.snooze();
            m_reminderCollection.addReminder(entry, false);
        } else if(direction == ItemTouchHelper.RIGHT) {
            if(entry.getRecurs()) {
                entry.complete();
                m_reminderCollection.addReminder(entry, false);
            }
        }

        //reminderViewHolder.notify();
        //reminderViewHolder.notifyAll();
        m_recycleViewAdapter.bindViewHolder(reminderViewHolder, position);
        //reminderViewHolder.updateUI();
        m_recycleViewAdapter.notifyDataSetChanged();

    }


    // SwipeController.java
    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        if (actionState == ACTION_STATE_SWIPE) {
//            if (buttonShowedState != ButtonsState.GONE) {
//                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, BUTTON_WIDTH);
//                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -BUTTON_WIDTH);
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//            else {
//                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        }
//
//        if (buttonShowedState == ButtonsState.GONE) {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//        currentItemViewHolder = viewHolder;

        // ...
        drawButtons(c, viewHolder);
    }
//    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
//                if (swipeBack) {
//                    if (dX < -BUTTON_WIDTH) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
//                    else if (dX > BUTTON_WIDTH) buttonShowedState  = ButtonsState.LEFT_VISIBLE;
//
//                    if (buttonShowedState != ButtonsState.GONE) {
//                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                        setItemsClickable(recyclerView, false);
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                }
//                return false;
//            }
//        });
//    }
//
//    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
//                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            return false;
//                        }
//                    });
//                    setItemsClickable(recyclerView, true);
//                    swipeBack = false;
//
//                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
//                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
//                        }
//                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
//                        }
//                    }
//                    buttonShowedState = ButtonsState.GONE;
//                    currentItemViewHolder = null;
//                }
//                return false;
//            }
//        });
//    }
//
//    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
//        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
//            recyclerView.getChildAt(i).setClickable(isClickable);
//        }
//    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = BUTTON_WIDTH - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("SNOOZE", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText("COMPLETE", c, rightButton, p);

//        buttonInstance = null;
//        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//            buttonInstance = leftButton;
//        }
//        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//            buttonInstance = rightButton;
//        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 20;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

//    public void onDraw(Canvas c) {
//        if (currentItemViewHolder != null) {
//            drawButtons(c, currentItemViewHolder);
//        }
//    }
}
