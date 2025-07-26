package com.quanxiaoha.xiaohashu.auth.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {

    private LocalDateTime datetime;
    private String name;
}
