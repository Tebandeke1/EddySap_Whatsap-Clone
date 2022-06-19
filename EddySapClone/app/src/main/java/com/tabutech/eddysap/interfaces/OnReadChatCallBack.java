package com.tabutech.eddysap.interfaces;

import com.tabutech.eddysap.Model.Chats.Chat;

import java.util.List;

public interface OnReadChatCallBack {
    void onReadSuccess(List<Chat> chatList);
    void onReadFailed();
}
