# Domain Layer

A camada **Domain** é o "esqueleto" da aplicação. Ela contém as **entidades de negócio**, **enums** e **exceções de domínio**, Isso garante que as regras de negócio sejam puras e testáveis de forma isolada.

---

## Estrutura

```
domain/
├── model/
├── enums/
└── exception/
```

---

## Entidades (Models)

### `Manutencao`
### `Veiculo`
### `User`

Todas as entidades utiliza o padrão **Factory Method** com construtores privados para garantir que toda instância seja criada de forma válida.

## Enums

### `StatusManutencao`

```java
public enum StatusManutencao {
    PENDENTE,
    EM_REALIZACAO,
    CONCLUIDA
}
```

### `TipoVeiculo`

```java
public enum TipoVeiculo {
    LEVE,
    PESADO
}
```

---

## Exceções de Domínio

| `BusinessException` | Classe base abstrata |   |
| `InvalidCredentialsException` | Email ou senha inválidos no login | `401` |
| `ManutencaoNotFoundException` | Manutenção não encontrada pelo ID | `404` |
| `UserAlreadyExistsException` | Email já cadastrado no registro | `409` |
| `UserNotFoundException` | Usuário não encontrado pelo ID | `404` |
| `VeiculoAlreadyExistsException` | Placa já cadastrada | `409` |
| `VeiculoNotFoundException` | Veículo não encontrado pelo ID | `404` |

---