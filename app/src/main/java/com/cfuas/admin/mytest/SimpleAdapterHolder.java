package com.cfuas.admin.mytest;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author sunJi
 * 简单万能adapter
 */
public class SimpleAdapterHolder {

	@SuppressWarnings("unused")
	private int mPosition;
	private View mConvertView;
	SparseArray<View> mMembers;

	public View getmConvertView() {
		return mConvertView;
	}

	public SimpleAdapterHolder() {

	}

	private SimpleAdapterHolder(ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mMembers = new SparseArray<View>();
		mConvertView = LayoutInflater.from(parent.getContext()).inflate(
				layoutId, parent, false);

		mConvertView.setTag(this);
	}

	public static SimpleAdapterHolder get(View convertView, ViewGroup parent,
										  int layoutId, int position) {
		if (convertView == null) {
			return new SimpleAdapterHolder(parent, layoutId, position);
		} else {
			return (SimpleAdapterHolder) convertView.getTag();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mMembers.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mMembers.put(viewId, view);
		}
		return (T) view;
	}
}
