@extends('admin.layout.base')

@section('title', 'Payment History ')

@section('content')

    <div class="content-area py-1">
        <div class="container-fluid">
            <div class="box box-block bg-white">
                <h5 class="mb-1">Payment History</h5>
                <table class="table table-striped table-bordered dataTable" id="table-2">
                    <thead>
                        <tr>
                            <th>Request ID</th>
                            <th>Transaction ID</th>
                            <th>From</th>
                            <th>To</th>
                            <th>Total Amount</th>
                            <th>Payment Mode</th>
                            <th>Payment Status</th>
                        </tr>
                    </thead>
                    <tbody>
                    @foreach($payments as $index => $payment)
                        <tr>
                            <td>{{$payment->id}}</td>
                            <td>{{$payment->payment->payment_id}}</td>
                            <td>{{$payment->user->first_name}} {{$payment->user->last_name}}</td>
                            <td>{{$payment->provider->first_name}} {{$payment->provider->last_name}}</td>
                            <td>{{currency($payment->payment->total)}}</td>
                            <td>{{$payment->payment_mode}}</td>
                            <td>
                                @if($payment->paid)
                                    Paid
                                @else
                                    Not Paid
                                @endif
                            </td>
                        </tr>
                    @endforeach
                    </tbody>
                    <tfoot>
                        <tr>
                            <th>Request ID</th>
                            <th>Transaction ID</th>
                            <th>From</th>
                            <th>To</th>
                            <th>Total Amount</th>
                            <th>Payment Mode</th>
                            <th>Payment Status</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
            
        </div>
    </div>
@endsection