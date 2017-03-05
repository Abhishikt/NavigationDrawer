package com.resultstrack.navigationdrawer1.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by abhishikt sk on 3/3/2017.
 */

public class OfflineData {

    public static List<ChildReg> getOfflineChildRegistrationData()
    {
        List<ChildReg> lstChildReg = new ArrayList<ChildReg>();
        try{
            ChildReg oChildReg = new ChildReg();
            lstChildReg = oChildReg.getAllChildren();
            return lstChildReg;
        }catch (Exception e)
        {
            return lstChildReg;
        }
    }

    public static List<CommitteeMember> getOfflineCommitteeMemberData()
    {
        List<CommitteeMember> lstCommitteeMember = new ArrayList<CommitteeMember>();
        try{
            CommitteeMember oCommitteeMember = new CommitteeMember();
            lstCommitteeMember = oCommitteeMember.getAllMembers();
            return lstCommitteeMember;
        }catch (Exception e)
        {
            return lstCommitteeMember;
        }
    }
}
