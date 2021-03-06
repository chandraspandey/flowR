
/**
 * 
 * 
 * @author Chandra Shekhar Pandey
 * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
 */

package org.flowr.framework.core.node.io.network;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.flowr.framework.core.exception.ConfigurationException;
import org.flowr.framework.core.node.io.endpoint.NodeConfig;
import org.flowr.framework.core.node.io.flow.ByteEnumerableType;
import org.flowr.framework.core.node.io.flow.protocol.FlowSocketChannel;

public interface NodeChannel extends NodeChannelLifeCycle{

   String getChannelName();
    
    ByteBuffer readFromNetwork();

    void writeToNetwork();
    
    SocketAddress getSocketAddress(ChannelFlowType channelFlowType);

    ChannelStatus connect(SocketAddress serverSocketAddress,ChannelFlowType channelFlowType) 
            throws ConfigurationException;
    
    <T> Set<SocketOption<T>> getDefaultSupportedOption(ChannelFlowType channelFlowType);

    void configureSocketOption(ChannelFlowType channelFlowType);
    
    ChannelStatus getChannelStatus(ChannelFlowType channelFlowType);
    
    ChannelState getChannelState(ChannelFlowType channelFlowType); 
    
    NodeConfig getNodeConfig();
    
    NetworkGroup getNetworkGroup();
    
    FlowSocketChannel getSocketChannel(ChannelFlowType channelFlowType);

    static NodeChannelSocketOption getNodeChannelSocketOption() {
        
        return NodeChannelSocketOption.getInstance();
    }
    
    /**
     * 
     * Defines channel flow type for operational handling
     * @author Chandra Shekhar Pandey
     * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
     *
     */
    public enum ChannelFlowType implements ByteEnumerableType{
        NONE(0),
        INBOUND(1),
        OUTBOUND(2),
        INTERRUPT(3);
        
        private byte code;
        
        ChannelFlowType(int code){
            
            this.code = (byte)code;
        }

        @Override
        public byte getCode() {
            
            return code;
        }   
        
        public static ChannelFlowType getType(int code) {
            
            ChannelFlowType channelFlowType = null;
            
            switch((byte) code) {
                
                case 0:{
                    channelFlowType = NONE;
                    break;
                }case 1:{
                    channelFlowType = INBOUND;
                    break;
                }case 2:{
                    channelFlowType = OUTBOUND;
                    break;
                }case 3:{
                    channelFlowType = INTERRUPT;
                    break;
                }default :{
                    channelFlowType = NONE;
                    break;
                }           
            }
            
            return channelFlowType;
        }
    }
    
    /**
     * 
     * Defines channel status for operational handling
     * @author Chandra Shekhar Pandey
     * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
     *
     */
    public enum ChannelStatus implements ByteEnumerableType{
        DEFAULT(0),
        INITIALIZED(1),
        REGISTERED(2),
        SEEK(3),
        CANCEL(4),
        READ(5),
        WRITE(6),
        CONNECTED(7),
        DISCONNECTED(8),
        ERROR(9);
        
        private byte code;
        
        ChannelStatus(int code){
            
            this.code = (byte)code;
        }

        @Override
        public byte getCode() {
            
            return code;
        }   
        
        public static ChannelStatus getType(int code) {
            
            ChannelStatus channelStatus = null;
            
            switch((byte) code) {
                
                case 0:{
                    channelStatus = DEFAULT;
                    break;
                }case 1:{
                    channelStatus = INITIALIZED;
                    break;
                }case 2:{
                    channelStatus = REGISTERED;
                    break;
                }case 3:{
                    channelStatus = SEEK;
                    break;
                }case 4:{
                    channelStatus = CANCEL;
                    break;
                }case 5:{
                    channelStatus = READ;
                    break;
                }case 6:{
                    channelStatus = WRITE;
                    break;
                }case 7:{
                    channelStatus = CONNECTED;
                    break;
                }case 8:{
                    channelStatus = DISCONNECTED;
                    break;
                }case 9:{
                    channelStatus = ERROR;
                    break;
                }default :{
                    channelStatus = DEFAULT;
                    break;
                }           
            }
            
            return channelStatus;
        }       
    }   
    
    /**
     * 
     * Defines channel state for operational handling
     * @author Chandra Shekhar Pandey
     * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
     *
     */
    public enum ChannelState implements ByteEnumerableType{
        DEFAULT(0),
        READ_INITIATED(1),
        READ_ERROR(2),
        READ_COMPLETED(3),
        WRITE_INITIATED(4),
        WRITE_ERROR(5),
        WRITE_COMPLETED(6);
        
        private byte code;
        
        ChannelState(int code){
            
            this.code = (byte)code;
        }

        @Override
        public byte getCode() {
            
            return code;
        }   
        
        public static ChannelState getType(int code) {
            
            ChannelState channelState = null;
            
            switch((byte) code) {
                
                case 0:{
                    channelState = DEFAULT;
                    break;
                }case 1:{
                    channelState = READ_INITIATED;
                    break;
                }case 2:{
                    channelState = READ_ERROR;
                    break;
                }case 3:{
                    channelState = READ_COMPLETED;
                    break;
                }case 4:{
                    channelState = WRITE_INITIATED;
                    break;
                }case 5:{
                    channelState = WRITE_ERROR;
                    break;
                }case 6:{
                    channelState = WRITE_COMPLETED;
                    break;
                }default :{
                    channelState = DEFAULT;
                    break;
                }           
            }
            
            return channelState;
        }       
    }
    
    /**
     * 
     * Defines channel type for operational handling
     * @author Chandra Shekhar Pandey
     * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
     *
     */
    public enum ChannelType  implements ByteEnumerableType{
        NONE(0),
        CLIENT(1),
        SERVER(2),
        PIPE(3);
        
        private byte code;
        
        ChannelType(int code){
            
            this.code = (byte)code;
        }

        @Override
        public byte getCode() {
            
            return code;
        }   
        
        public static ChannelType getType(int code) {
            
            ChannelType channelType = null;
            
            switch((byte) code) {
                
                case 0:{
                    channelType = NONE;
                    break;
                }case 1:{
                    channelType = CLIENT;
                    break;
                }case 2:{
                    channelType = SERVER;
                    break;
                }case 3:{
                    channelType = PIPE;
                    break;
                }default :{
                    channelType = NONE;
                    break;
                }           
            }
            
            return channelType;
        }
    }   
    
    /**
     * 
     * Defines channel protocol for data flow handling
     * @author Chandra Shekhar Pandey
     * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
     *
     */
    public enum ChannelProtocol  implements ByteEnumerableType{
        NONE(0),
        START(1),
        ACK(2),
        NACK(3),
        ERROR(4),
        TIMEOUT(5),
        END(6);
        
        private byte code;
        
        ChannelProtocol(int code){
            
            this.code = (byte)code;
        }

        @Override
        public byte getCode() {
            
            return code;
        }   
        
        public static ChannelProtocol getType(int code) {
            
            ChannelProtocol channelProtocol = null;
            
            switch((byte) code) {
                
                case 0:{
                    channelProtocol = NONE;
                    break;
                }case 1:{
                    channelProtocol = START;
                    break;
                }case 2:{
                    channelProtocol = ACK;
                    break;
                }case 3:{
                    channelProtocol = NACK;
                    break;
                }case 4:{
                    channelProtocol = ERROR;
                    break;
                }case 5:{
                    channelProtocol = TIMEOUT;
                    break;
                }case 6:{
                    channelProtocol = END;
                    break;
                }default :{
                    channelProtocol = NONE;
                    break;
                }           
            }
            
            return channelProtocol;
        }               
    }   
    
 

    public final class NodeChannelSocketOption{

        private static NodeChannelSocketOption nodeChannelSocketOption;
        private SocketOption<Boolean> lifeOption            = StandardSocketOptions.SO_KEEPALIVE;
        private SocketOption<Boolean> addressOption         = StandardSocketOptions.SO_REUSEADDR;
        private SocketOption<Boolean> portOption            = StandardSocketOptions.SO_REUSEPORT;
        private SocketOption<Boolean> tcpOption             = StandardSocketOptions.TCP_NODELAY;
        private SocketOption<Integer> sendBufferOption      = StandardSocketOptions.SO_SNDBUF;
        private SocketOption<Integer> recieveBufferOption   = StandardSocketOptions.SO_RCVBUF;
        private Set<SocketOption<?>> configuredOptions      = new HashSet<>();
               
       private NodeChannelSocketOption() {
            
            configuredOptions.add(lifeOption);
            configuredOptions.add(addressOption);
            configuredOptions.add(portOption);
            configuredOptions.add(tcpOption);
            configuredOptions.add(sendBufferOption);
            configuredOptions.add(recieveBufferOption);         
        }
        
        public static NodeChannelSocketOption getInstance() {
                                    
            if(nodeChannelSocketOption == null ) {
                
                nodeChannelSocketOption = new NodeChannelSocketOption();
            }
            
            return nodeChannelSocketOption;
        }
        
        public Channel configure(Channel channel, Set<SocketOption<?>> supportedOptions ) {
            
            if(supportedOptions == null) {
                supportedOptions = configuredOptions;
            }       
        
            Iterator<SocketOption<?>> optionIterator = supportedOptions.iterator();
            
            while(optionIterator.hasNext()) {
                
                SocketOption<?> socketOption = optionIterator.next();
                
                try {
                    
                    if(socketOption.type() == Boolean.class) {
                        
                        configureBooleanOption(channel, socketOption);
                    }else if(socketOption.type() == Integer.class){
                        
                        configureIntegerOption(channel, socketOption);
                        
                    }else {
                        channel = null;
                    }

                } catch (IOException | UnsupportedOperationException e) {
                    Logger.getRootLogger().error(e);
                    optionIterator.remove();
                }
            }
            
            return channel;         
        }

        private static void configureBooleanOption(Channel channel, SocketOption<?> socketOption) throws IOException {
            
            @SuppressWarnings("unchecked")
            SocketOption<Boolean> sOption = (SocketOption<Boolean>) socketOption;
            
            if(channel instanceof AsynchronousServerSocketChannel) {
                                          
                ((AsynchronousServerSocketChannel)channel).setOption(sOption, Boolean.TRUE); 
                
            }
            
            if(channel instanceof AsynchronousSocketChannel) {
                ((AsynchronousSocketChannel)channel).setOption(sOption, Boolean.TRUE);
            }
        }

        private static void configureIntegerOption(Channel channel, SocketOption<?> socketOption) throws IOException {
           
            @SuppressWarnings("unchecked")
            SocketOption<Integer> sOption = (SocketOption<Integer>) socketOption;                       

            if(channel instanceof AsynchronousServerSocketChannel) {      
                
                  ((AsynchronousServerSocketChannel)channel).setOption(sOption, Integer.valueOf("1024"));
                    
            }
            
            if(channel instanceof AsynchronousSocketChannel) {
               
                ((AsynchronousSocketChannel)channel).setOption(sOption, Integer.valueOf("1024"));
            }
        }        
    }

}
