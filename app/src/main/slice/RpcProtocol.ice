#pragma once

module team
{
    module benchem
    {
        module communication
        {
            interface JsonServicePortal
            {
                string invoke(string requestBody);
            }

            interface JsonServiceCenter
            {
                void register(string clientTag, JsonServicePortal* callBack);

                void unRegister(string clientTag);

                void cast(string requestBody);

                string forward(string clientTag, string requestBody);
            }
        }
    }
}