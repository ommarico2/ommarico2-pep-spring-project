package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Component
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    public List<Message> getMessagesByAccount(int id) {
        return messageRepository.findMessageByPostedBy(id);
    }

    public Message postMessage(Message newMessage) {
        if (newMessage.getMessageText().length() > 0 && newMessage.getMessageText().length() < 255) {
            Optional<Account> optAcc = accountRepository.findById(newMessage.getPostedBy());
            if (optAcc.isPresent()) {
                return messageRepository.save(newMessage);
            }

        }
        return null;

    }

    public Integer deleteMessageById(Integer deleteId){
        if(messageRepository.findById(deleteId).isPresent()){
            messageRepository.deleteById(deleteId);
            return 1;
        } else {
            return null;
        }       
    }
    
    
    public class MessageNotFoundException extends RuntimeException {
        public MessageNotFoundException(String message) {
            super(message);
        }
    }
    
    

    public int updateMessage(int message_id, String newMessage) {
        if (newMessage.length() > 0 && newMessage.length() < 255) {
            Optional<Message> optMess = messageRepository.findById(message_id);
            if (optMess.isPresent()) {
                Message message = optMess.get();
                message.setMessageText(newMessage);
                messageRepository.save(message);
                return 1;
            }
        }
        return 0;

    }
}