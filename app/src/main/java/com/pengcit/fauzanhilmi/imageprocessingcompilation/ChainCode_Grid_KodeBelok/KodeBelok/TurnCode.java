package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid_KodeBelok.KodeBelok;

import java.util.ArrayList;

/**
 * Created by Fauzan Hilmi on 25/10/2015.
 */
public class TurnCode {
    public static int getTurn(int from,int to){
        int diff=to-from;
        while (diff>=8)diff-=8;while (diff<0)diff+=8;
        return diff;
    }

    public static ArrayList<Integer> processChainCode(ArrayList<Integer> in_chain){
        ArrayList<Integer> res=new ArrayList<>();
        int prim_code = in_chain.get(1);
        int prim_sum=0;
        int rgh_sum=0,lef_sum=0,back_sum=0;
        int turn;
        double limit;
        for (int ch:in_chain){
            turn=getTurn(prim_code, ch);
            limit=prim_sum/2;
            if (turn==0){
                prim_sum++;
            } else if ((0<turn)||(turn<4)){
                if (turn==1)back_sum--;
                else if (turn==3)back_sum++;
                rgh_sum++;
            } else if ((4<turn)||(turn<8)){
                if (turn==7)back_sum--;
                else if (turn==5)back_sum++;
                lef_sum++;
            } else if (turn==4){
                back_sum++;
            }
            if ((rgh_sum-lef_sum)>limit){
                if (prim_sum>back_sum)for (int i=0;i<(prim_sum-back_sum);i++)res.add(prim_code);
                prim_code=(prim_code+2)%8;
                prim_sum=rgh_sum-lef_sum;
                rgh_sum=back_sum>0?back_sum:0;
                lef_sum=0;
                back_sum=0;
//				for (int i=0;i<rgh_sum;i++)res.add((prim_code+2)%8);
//				if (back_sum>prim_sum)for (int i=0;i<(back_sum-prim_sum);i++)res.add((prim_code+4)%8);
            } else if ((lef_sum-rgh_sum)>limit){
                if (prim_sum>back_sum)for (int i=0;i<(prim_sum-back_sum);i++)res.add(prim_code);
                prim_code=(prim_code+6)%8;
                prim_sum=lef_sum-rgh_sum;
                lef_sum=back_sum>0?back_sum:0;
                rgh_sum=0;
                back_sum=0;
//				for (int i=0;i<lef_sum;i++)res.add((prim_code+6)%8);
//				if (back_sum>prim_sum)for (int i=0;i<(back_sum-prim_sum);i++)res.add((prim_code+4)%8);
            } else if (back_sum>limit){
                for (int i=0;i<(prim_sum-back_sum);i++)res.add(prim_code);
                if (rgh_sum>lef_sum)for (int i=0;i<(rgh_sum-lef_sum);i++)res.add((prim_code+2)%8);
                else if (lef_sum>rgh_sum)for (int i=0;i<(lef_sum-rgh_sum);i++)res.add((prim_code+6)%8);
                prim_code=(prim_code+4)%8;
                prim_sum=back_sum;
                lef_sum=0;rgh_sum=0;
                back_sum=0;
//				for (int i=0;i<lef_sum;i++)res.add((prim_code+4)%8);
            }
        }
        if (prim_sum>0){
            for (int i=0;i<(prim_sum-back_sum);i++)res.add(prim_code);
            if (rgh_sum>lef_sum)for (int i=0;i<(rgh_sum-lef_sum);i++)res.add((prim_code+2)%8);
            else if (lef_sum>rgh_sum)for (int i=0;i<(lef_sum-rgh_sum);i++)res.add((prim_code+6)%8);
            for (int i=0;i<(back_sum-prim_sum);i++)res.add((prim_code+4)%8);
        }
        return res;
    }

    public static ArrayList<Integer> toTurnCode(ArrayList<Integer> in_chain){
        ArrayList<Integer> res=new ArrayList<>();
        int curr = in_chain.get(1);
        for (int ch:in_chain){
            if ((0<getTurn(curr, ch))&&(getTurn(curr, ch)<4)){
                res.add(0);
                curr=ch;
            } else if ((4<getTurn(curr, ch))&&(getTurn(curr, ch)<8)){
                res.add(1);
                curr=ch;
            }
        }
        return res;
    }
}
