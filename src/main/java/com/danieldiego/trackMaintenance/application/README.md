# Application Layer


A camada **Application** contém os **casos de uso** da aplicação   a orquestração da lógica de negócio. Ela define as **Portas (Ports)** que representam os contratos de entrada e saída na Arquitetura Hexagonal, e os **Services** que implementam os use cases consumindo essas portas.
 A única dependência externa é `org.springframework.data.domain` para paginação.

## Diagrama de Dependências

```
┌──────────────┐         ┌─────────────────┐
│   Adapter    │ ──────► │   Application   │
│ (Controllers)│         │   (Services)    │
└──────────────┘         └────────┬────────┘
                                  │ usa
                         ┌────────▼────────┐
                         │     Ports       │
                         │  (Interfaces)   │
                         └────────┬────────┘
                                  │ implementado por
                         ┌────────▼────────┐
                         │ Infrastructure  │
                         │  (Adapters)     │
                         └─────────────────┘
```

A camada Application **define** as Ports e as **consome**. A camada Infrastructure **implementa** as Ports. Isso inverte a dependência   o core nunca depende da infraestrutura.

---

## Padrões Aplicados

| Padrão | Onde |
|--------|------|
| **Ports and Adapters** | Interfaces em `Interface/` como portas hexagonais |
| **Use Case / Service** | Services como orquestradores dos casos de uso |
| **Command Pattern** | Records `*Command` representam intenções de ação |
| **Output DTO** | Records `*Output` desacoplam domain das respostas HTTP |
| **Dependency Inversion** | Services dependem de portas (abstrações), não de implementações |
| **Single Responsibility** | Cada service cuida de um agregado (User, Veiculo, Manutencao, Dashboard) |
