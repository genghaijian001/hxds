package com.example.hxds.nebula.controller.form;

import com.example.hxds.nebula.controller.vo.InsertOrderGpsVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;

@Data
@Schema(description = "添加订单GPS记录的表单")
public class InsertOrderGpsForm {

    @NotEmpty(message = "list不能为空")
    @Schema(description = "GPS数据")
    private ArrayList<@Valid InsertOrderGpsVo> list;

}
