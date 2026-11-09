package com.orderprocessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.application.service.OrderService;
import com.orderprocessor.domain.exception.InvalidOrderException;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void deveRetornar409QuandoOcorreConflitoDeVersao() throws Exception {
        when(orderService.receberPedido(any(CreateOrderRequest.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException("Product", 1L));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "customerName": "Cliente Teste",
                            "paymentMethod": "CREDIT_CARD",
                            "items": [
                                {
                                    "productId": 1,
                                    "quantity": 1
                                }
                            ]
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(content().string(
                        "O produto foi alterado por outra operação. Tente novamente."));
    }

    @Test
    void deveRetornar400QuandoPedidoPossuiDadosInvalidos() throws Exception {

        when(orderService.receberPedido(any(CreateOrderRequest.class)))
                .thenThrow(new InvalidOrderException());

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "customerName": "Cliente Teste",
                            "paymentMethod": "CREDIT_CARD",
                            "items": [
                                {
                                    "productId": 1,
                                    "quantity": 1
                                }
                            ]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Os dados sao invalidos!"));
    }

}