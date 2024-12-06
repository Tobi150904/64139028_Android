package vn.ngoviethoang.duancuoiky.Ui.AddTransaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;

public class AddTransactionViewModel extends AndroidViewModel {
    private final GiaoDichRepository giaoDichRepository;
    private final MutableLiveData<GiaoDich> giaoDichLiveData;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> selectedAccount;
    private final MutableLiveData<String> selectedCategory;
    private final MutableLiveData<String> selectedDate;
    private final MutableLiveData<String> selectedCard;

    public AddTransactionViewModel(@NonNull Application application) {
        super(application);
        giaoDichRepository = new GiaoDichRepository(application);
        giaoDichLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        selectedAccount = new MutableLiveData<>();
        selectedCategory = new MutableLiveData<>();
        selectedDate = new MutableLiveData<>();
        selectedCard = new MutableLiveData<>();
    }

    public void addTransaction(GiaoDich giaoDich) {
        giaoDichRepository.insertGiaoDich(giaoDich, new GiaoDichRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                giaoDichLiveData.setValue(giaoDich);
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<GiaoDich> getGiaoDich() {
        return giaoDichLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void updateTransaction(GiaoDich giaoDich) {
        giaoDichRepository.updateGiaoDich(giaoDich, new GiaoDichRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                giaoDichLiveData.setValue(giaoDich);
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<String> getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(String account) {
        selectedAccount.setValue(account);
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String category) {
        selectedCategory.setValue(category);
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(String card) {
        selectedCard.setValue(card);
    }
}