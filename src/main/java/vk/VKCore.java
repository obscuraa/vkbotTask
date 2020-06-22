package vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import com.vk.api.sdk.objects.messages.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class VKCore {
    private VkApiClient vk;
    private static int ts;
    private GroupActor actor;
    private static int maxMsgId = -1;

    public VKCore() throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        // Загрузка конфигураций

        Properties prop = new Properties();
        int groupId;
        String access_token;
        try {
            prop.load(new FileInputStream("src/main/resources/vkconfig.properties"));
            groupId = Integer.valueOf(prop.getProperty("group_id"));
            access_token = prop.getProperty("access_token");
            actor = new GroupActor(groupId, access_token);

            ts = vk.messages().getLongPollServer(actor).execute().getTs();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
    }

    public GroupActor getActor() {
        return actor;
    }
    public VkApiClient getVk() {
        return vk;
    }
    public Message getMessage() throws ClientException, ApiException {
        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages()
                .getLongPollHistory(actor)
                .ts(ts);
        if (maxMsgId > 0){
            eventsQuery.maxMsgId(maxMsgId);
        }

        List<Message> messages = eventsQuery
                .execute()
                .getMessages()
                .getMessages();

        if (!messages.isEmpty()){
            try {
                ts =  vk.messages()
                        .getLongPollServer(actor)
                        .execute()
                        .getTs();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        if (!messages.isEmpty() && !messages.get(0).isOut()) {
            int messageId = messages.get(0).getId();
            if (messageId > maxMsgId){
                maxMsgId = messageId;
            }
            return messages.get(0);
        }
        return null;
    }
}