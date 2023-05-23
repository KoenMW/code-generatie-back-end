package com.Inholland.NovaBank.model;

import lombok.Data;

@Data
public class UserIdRequestBody {

        private long userId;

        public UserIdRequestBody(long userId) {
            this.userId = userId;
        }

        public UserIdRequestBody() {

        }

        public long getUserId() {
            return userId;
        }
}
