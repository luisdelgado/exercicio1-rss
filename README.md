# Tarefa #2 - RSS 

A ideia deste exercício é aplicar os conceitos de `Service`, `BroadcastReceiver`, `SQLite`, `RecyclerView`, entre outros. 

A aplicação RSS disponível neste repositório é uma versão atualizada da que foi passada como exercício anterior, mas *ainda incompleta*. Observe os passos listados abaixo. 
Esta versão está usando a classe `SQLiteRSSHelper` para gerenciar o banco de dados `SQLite` como forma de persistir os dados. 
Isto é, após o download e parsing do RSS, a lista de itens do feed está sendo armazenada no banco, ao invés de exibida diretamente na tela. 
Veja que existem dois objetos `AsyncTask` agora. Um é responsável por carregar o XML da internet e salvar no banco. Se tudo correr bem, outro AsyncTask executa para carregar as notícias do banco de dados e exibir na tela. 

Caso você não tenha feito a [Tarefa #1](https://github.com/if1001/exercicio1-rss) use este projeto como base. Do contrário, sugiro que você pegue a estrutura deste projeto como base para modificar o seu projeto. 
Siga os passos na ordem sugerida e marque mais abaixo, na sua resposta, quais os passos completados. 
Para entregar o exercício, responda o [formulário de entrega](https://docs.google.com/forms/d/e/1FAIpQLSekCO9cBY3FepSBG3KyQasSMkZQvgS5ikN29x4KnyN9xYSj6Q/viewform) até 16/04/2018, às 23h59.

  10. A classe `SQLiteRSSHelper` já tem toda a configuração do banco. No entanto, ainda é necessário implementar os métodos de manipulação do banco de dados (da linha 73 em diante), que estão em aberto ainda. A implementação do método `getItems` deve retornar apenas os itens não lidos;
  11. Complete a implementação do método `onItemClick` (linha 74 em diante de `MainActivity`), de forma que ao clicar, o link seja aberto no navegador e a notícia seja marcada como lida no banco;
  12. Altere a aplicação de forma a usar um `Service` para fazer o download e persistência dos itens do feed no banco. Ou seja, a ideia aqui é mover o código que atualmente está no `AsyncTask` que carrega o feed a partir da internet para um `Service`. Dica: use `IntentService`;
  13. Ao finalizar a tarefa, o `Service` deve enviar um broadcast avisando que terminou;
  14. Use um `BroadcastReceiver` registrado dinamicamente, para quando o usuário estiver com o app em primeiro plano, a atualização da lista de itens ser feita de forma automática;
  15. Se o usuário não estiver com o app em primeiro plano, um outro `BroadcastReceiver` registrado estaticamente deve exibir uma notificação, apenas se houver alguma notícia nova;
  16. Usando `SharedPreferences` e `PreferenceFragment`, defina um outro item na tela de configurações para estabelecer uma periodicidade para o carregamento de notícias, incluindo as seguintes possibilidades: 30 min / 1h / 3h / 6h / 12h / 24h (tem que adicionar outra preference no XML). Por enquanto basta apenas salvar a preferência do usuário, não estamos de fato agendando a tarefa ainda;
  17. *OPCIONAL* Faça com que a aplicação passe a usar um `RecyclerView`, ao invés de `ListView`. Sugestão: Use `SortedList` para ordenar itens cronologicamente (do mais recente para o mais antigo).

---

# Orientações

  - Comente o código que você desenvolver, explicando o que cada parte faz.
  - Entregue o exercício *mesmo que não tenha completado todos os itens* listados. Marque abaixo apenas o que completou.

----

# Status

| Passo | Completou? |
| ------ | ------ |
| 10 | **não** |
| 11 | **não** |
| 12 | **não** |
| 13 | **não** |
| 14 | **não** |
| 15 | **não** |
| 16 | **não** |
| 17 | **não** |

# Tarefa #1 - RSS 

Esta tarefa envolve os conceitos de UI widgets, AsyncTasks, Custom Adapters, Intents, Permissions, e SharedPreferences. 
Faça um clone ou fork deste projeto, siga os passos na ordem sugerida e marque mais abaixo, na sua resposta, quais os passos completados. 
Para entregar o exercício, responda o [formulário de entrega](https://docs.google.com/forms/d/e/1FAIpQLSdQksHsbYqlciFS5JHAGlODKsXAY0MUUlA33I_Aur3DkbmMDg/viewform) até 02/04/2018, às 23h59.

  1. Teste a aplicação e certifique-se de que está tudo funcionando.
  2. Altere a aplicação RSS de forma que passe a processar o arquivo XML usando a classe `ParserRSS`.
  3. Uma vez processado o XML por meio do parser, é retornado um objeto do tipo `List<ItemRSS>`. 
  4. Use este objeto para popular um `ListView` por meio de um `Adapter` --- o widget deve manter o mesmo id do TextView (`conteudoRSS`).
  5. Inicialmente, use um `ArrayAdapter<ItemRSS>` para confirmar que está funcionando. 
  6. Altere o `ListView`, para mostrar título e data para cada item RSS, por meio de um Adapter personalizado, usando o layout em `res/layout/itemlista.xml` como base. Este layout não deve ser alterado.
  7. Faça com que, ao clicar em um título, o usuário seja direcionado para o navegador. Opcionalmente, pode abrir em uma nova activity com `WebView`.
  8. Modifique a aplicação para que passe a carregar o endereço do feed a partir de uma `SharedPreferences` com a chave `rssfeed`. O endereço padrão para o feed está disponível em res/values/strings.xml.
  9. Inclua a possibilidade de alterar a `SharedPreference` (`rssfeed`) incluindo um botão na `ActionBar` da aplicação. Ao clicar no botão, abra `PreferenciasActivity`, que deve exibir uma `PreferenceScreen` gerada automaticamente por meio de um `Fragment` que estende a classe `PreferenceFragment`, como visto em sala. Use o arquivo em `res/xml/preferencias.xml` para definir a tela.

---

# Orientações

  - Comente o código que você desenvolver, explicando o que cada parte faz.
  - Entregue o exercício *mesmo que não tenha completado todos os itens* listados. Marque abaixo apenas o que completou.

----

# Status

| Passo | Completou? |
| ------ | ------ |
| 1 | **sim** |
| 2 | **sim** |
| 3 | **sim** |
| 4 | **sim** |
| 5 | **sim** |
| 6 | **sim** |
| 7 | **sim** |
| 8 | **sim** |
| 9 | **sim** |
